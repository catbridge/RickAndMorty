package com.example.rickandmorty.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.model.CharacterDetails
import com.example.rickandmorty.domain.repository.CharacterLocalRepository
import com.example.rickandmorty.domain.usecase.GetAllCharactersFromDBUseCase
import kotlinx.coroutines.flow.*

class FavouriteViewModel(private val getAllCharactersFromDBUseCase: GetAllCharactersFromDBUseCase) : ViewModel() {

    val characterDaoFlow = flow {
        getAllCharactersFromDBUseCase()
            .onSuccess {
                emit(it)
            }.getOrDefault(emptyList())
    }
}