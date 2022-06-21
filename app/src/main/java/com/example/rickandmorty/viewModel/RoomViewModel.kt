package com.example.rickandmorty.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.repository.CharacterLocalRepository
import com.example.rickandmorty.domain.usecase.GetAllCharactersFromDBUseCase
import kotlinx.coroutines.flow.*

class RoomViewModel(private val getAllCharactersFromDBUseCase: GetAllCharactersFromDBUseCase) : ViewModel() {

    val characterDaoFlow: Flow<List<Character>> = flow {
        emit(getAllCharactersFromDBUseCase())
    }.shareIn(viewModelScope, started = SharingStarted.Eagerly, replay = 1)
}