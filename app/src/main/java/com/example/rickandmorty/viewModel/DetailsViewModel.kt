package com.example.rickandmorty.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.domain.model.CharacterDetails
import com.example.rickandmorty.domain.model.Episode
import com.example.rickandmorty.domain.usecase.*
import com.example.rickandmorty.domain.model.LceState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val getCharacterDetailsUseCase: GetCharacterDetailsUseCase,
    private val getEpisodesUseCase: GetEpisodesUseCase,
    private val insertCharacterToDBUseCase: InsertCharacterToDBUseCase,
    private val deleteCharacterFromDBUseCase: DeleteCharacterFromDBUseCase,
    private val getCharacterDetailsFromDBUseCase: GetCharacterDetailsFromDBUseCase,
    private val id: Int
) : ViewModel() {

    private val favouriteFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val characterDetailsFlow = MutableSharedFlow<LceState<CharacterDetails>>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    init {
        viewModelScope.launch {
            getCharacterDetailsFromDBUseCase(id).fold(
                onSuccess = {
                    characterDetailsFlow.tryEmit(LceState.Content(it))
                },
                onFailure = {
                    val character = getCharacterDetailsUseCase(id)
                        character.fold(
                        onSuccess = {
                            characterDetailsFlow.tryEmit(LceState.Content(it))
                        },
                        onFailure = {
                            characterDetailsFlow.tryEmit(LceState.Error(it))
                        }
                    )
                }
            )
        }

        favouriteFlow.zip(characterDetailsFlow.
        filterIsInstance<LceState.Content<CharacterDetails>>()
        ) { _, characterDetails -> characterDetails.value
        }.onEach { character ->
            character.isFavourite = !character.isFavourite
            if (character.isFavourite) {
                insertCharacterToDBUseCase(character)
            } else {
                deleteCharacterFromDBUseCase(character)
            }
            characterDetailsFlow.tryEmit(LceState.Content(character.copy(isFavourite = character.isFavourite)))
        }.launchIn(viewModelScope)
    }

    val episodesFlow = flow {
        val characterDetails = getCharacterDetailsUseCase(id)
        getEpisodesUseCase(
            characterDetails.map {
                it.episode.map { it.substringAfterLast("/").toInt() }
            }
                .getOrDefault(emptyList())
        )
            .fold(
                onSuccess = {
                    emit(LceState.Content(it))
                },
                onFailure = {
                    emit(LceState.Error(it))
                }
            )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, initialValue = LceState.Loading)


    fun onFavouriteClicked(){
        favouriteFlow.tryEmit(Unit)
    }

}



