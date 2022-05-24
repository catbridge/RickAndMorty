package com.example.rickandmorty.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.model.Character
import com.example.rickandmorty.room.CharacterDao
import kotlinx.coroutines.flow.*

class RoomViewModel(characterDao: CharacterDao) : ViewModel() {

    val characterDaoFlow: Flow<List<Character>> = flow {
        emit(characterDao.getAllCharacters())
    }.shareIn(viewModelScope, started =  SharingStarted.Eagerly, replay = 1)
}