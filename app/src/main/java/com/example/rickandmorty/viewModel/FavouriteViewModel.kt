package com.example.rickandmorty.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.model.CharacterDetails
import com.example.rickandmorty.domain.model.LceState
import com.example.rickandmorty.domain.repository.CharacterLocalRepository
import com.example.rickandmorty.domain.usecase.GetAllCharactersFromDBUseCase
import kotlinx.coroutines.flow.*

class FavouriteViewModel(private val getAllCharactersFromDBUseCase: GetAllCharactersFromDBUseCase) : ViewModel() {

    private val _lceFlow = MutableStateFlow(LceState.Loading)
    val lceFlow = _lceFlow.asStateFlow()

    val characterDaoFlow = flow {
        emit(LceState.Loading)
        val state = getAllCharactersFromDBUseCase()
            .fold(
                onSuccess = {
                    LceState.Content(it)
                },
                onFailure = {
                    LceState.Error(it)
                }
            )
        emit(state)
    }
}