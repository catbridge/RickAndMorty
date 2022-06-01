package com.example.rickandmorty.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.usecase.GetCharactersByIdUseCase
import com.example.rickandmorty.domain.usecase.GetExtraItemUseCase
import com.example.rickandmorty.domain.usecase.GetLocationUseCase
import kotlinx.coroutines.flow.*

class ResidentsViewModel(
    private val getLocationUseCase: GetLocationUseCase,
    private val getCharactersByIdUseCase: GetCharactersByIdUseCase,
    private val getExtraItemUseCase: GetExtraItemUseCase,
    private val id: Int
) : ViewModel() {


    val residentsFlow: Flow<List<Character>> = flow {
        val numbers = getNumbers()
        if (numbers.isNotEmpty()) {
            emit(getCharactersByIdUseCase(getNumbers()))
        } else {
            emit(listOf(getExtraItemUseCase()))
        }
    }.shareIn(viewModelScope, started = SharingStarted.Eagerly, replay = 1)


    private suspend fun getNumbers(): List<Int> {
        val characterNumbers = getLocationUseCase(id).residents.map {
            it.substringAfterLast("/")
                .toInt()
        }
        return characterNumbers
    }


}