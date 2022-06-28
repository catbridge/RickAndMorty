package com.example.rickandmorty.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.model.CharacterDetails
import com.example.rickandmorty.domain.model.Episode
import com.example.rickandmorty.domain.usecase.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

class DetailsViewModel(
    private val getCharacterDetailsUseCase: GetCharacterDetailsUseCase,
    private val getEpisodesUseCase: GetEpisodesUseCase,
    private val insertCharacterToDBUseCase: InsertCharacterToDBUseCase,
    private val deleteCharacterFromDBUseCase: DeleteCharacterFromDBUseCase,
    private val getCharacterDetailsFromDBUseCase: GetCharacterDetailsFromDBUseCase,
    private val id: Int
) : ViewModel() {



    val characterDetailsFlow = flow {
        getCharacterDetailsFromDBUseCase(id)
            .fold(
                onSuccess = {
                    emit(it)
                },
                onFailure = {
                    getCharacterDetailsUseCase(id).onSuccess { emit(it) }
                        .getOrDefault(emptyList<CharacterDetails>())
                }
            )
    }.shareIn(viewModelScope, started = SharingStarted.Eagerly, replay = 1)

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
                    emit(it)
                },
                onFailure = {
                    emptyList<List<Episode>>()
                }
            )

    }.shareIn(viewModelScope, SharingStarted.Eagerly, replay = 1)


    suspend fun insertCharacter(character: CharacterDetails){
        insertCharacterToDBUseCase(character)
    }

    suspend fun deleteCharacter(character: CharacterDetails){
        deleteCharacterFromDBUseCase(character)
    }
}



