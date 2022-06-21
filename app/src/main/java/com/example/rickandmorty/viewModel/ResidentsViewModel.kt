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
        val numbers = getNumbers().map { it }
            .getOrDefault(emptyList())
        val extraItem = getExtraItemUseCase().map { it }
            .getOrThrow()

        getCharactersByIdUseCase(numbers).fold(
            onSuccess = {
                emit(it)
            },
            onFailure = {
                emit(listOf(extraItem))
            }
        )

    }.shareIn(viewModelScope, started = SharingStarted.Eagerly, replay = 1)


    private suspend fun getNumbers(): Result<List<Int>> {
        return getLocationUseCase(id).map { it.residents.map {
            it.substringAfterLast("/").toInt()}
        }
    }
}