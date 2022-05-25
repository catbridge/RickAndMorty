package com.example.rickandmorty.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.model.CharacterDetails
import com.example.rickandmorty.model.Episode
import com.example.rickandmorty.repository.RickAndMortyRepository
import kotlinx.coroutines.flow.*

class DetailsViewModel(
    private val repository: RickAndMortyRepository,
    private val id: Int
) : ViewModel() {

    val characterDetailsFlow: Flow<CharacterDetails> = flow {
        emit(repository.getCharacterDetails(id))
    }.shareIn(viewModelScope, started = SharingStarted.Eagerly, replay = 1)


    val episodesFlow: Flow<List<Episode>> = flow {
        emit(repository.getEpisodeList(getNumbers()))
    }.shareIn(viewModelScope, started = SharingStarted.Eagerly, replay = 1)

    private suspend fun getNumbers(): List<Int> {
        val episodeNumbers = repository.getCharacterDetails(id).episode.map {
            it.substringAfterLast("/")
                .toInt()
        }
        return episodeNumbers
    }
}



