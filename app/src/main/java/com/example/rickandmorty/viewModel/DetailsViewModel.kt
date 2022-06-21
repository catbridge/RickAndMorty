package com.example.rickandmorty.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.domain.model.CharacterDetails
import com.example.rickandmorty.domain.model.Episode
import com.example.rickandmorty.domain.usecase.GetCharacterDetailsUseCase
import com.example.rickandmorty.domain.usecase.GetEpisodesUseCase
import kotlinx.coroutines.flow.*

class DetailsViewModel(
    private val getCharacterDetailsUseCase: GetCharacterDetailsUseCase,
    private val getEpisodesUseCase: GetEpisodesUseCase,
    private val id: Int
) : ViewModel() {

    val characterDetailsFlow: Flow<CharacterDetails> = flow {
        emit(getCharacterDetailsUseCase(id))
    }.shareIn(viewModelScope, started = SharingStarted.Eagerly, replay = 1)


    val episodesFlow: Flow<List<Episode>> = flow {
        val characterDetails = getCharacterDetailsUseCase(id)
        emit(getEpisodesUseCase(characterDetails.episode.map {
            it.substringAfterLast("/").toInt()
        }))
    }.shareIn(viewModelScope, started = SharingStarted.Eagerly, replay = 1)

}




