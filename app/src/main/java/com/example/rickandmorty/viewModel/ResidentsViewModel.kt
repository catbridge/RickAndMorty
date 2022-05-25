package com.example.rickandmorty.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.model.Character
import com.example.rickandmorty.model.ExtraRick
import com.example.rickandmorty.repository.RickAndMortyRepository
import kotlinx.coroutines.flow.*

class ResidentsViewModel(
    private val repository: RickAndMortyRepository,
    private val id: Int
) : ViewModel() {


    private val extraItem = ExtraRick()

    val residentsFlow: Flow<List<Character>> = flow {
        val numbers = getNumbers()
        if (numbers.isNotEmpty()) {
            emit(repository.getCharactersList(getNumbers()))
        } else {
            emit(listOf(Character(id = extraItem.id,name = extraItem.name, image = extraItem.image)))
        }
    }.shareIn(viewModelScope, started = SharingStarted.Eagerly, replay = 1)


    private suspend fun getNumbers(): List<Int> {
        val characterNumbers = repository.getLocation(id).residents.map {
            it.substringAfterLast("/")
                .toInt()
        }
        return characterNumbers
    }


}