package com.example.rickandmorty.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.model.LceState
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


    val residentsFlow: Flow <LceState<List<Character>>> = flow {
        val numbers = getLocationUseCase(id).map { it.residents.map { string ->
            string.substringAfterLast("/").toInt()}
        }.getOrDefault(emptyList())

        val extraItem = getExtraItemUseCase()
            .getOrThrow()

        val state = getCharactersByIdUseCase(numbers).fold(
            onSuccess = {
                LceState.Content(it)
            },
            onFailure = {
                LceState.Content(listOf(extraItem))
            }
        )
        emit(state)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, initialValue = LceState.Loading)

}