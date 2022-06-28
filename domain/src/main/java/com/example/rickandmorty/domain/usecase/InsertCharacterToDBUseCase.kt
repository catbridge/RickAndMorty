package com.example.rickandmorty.domain.usecase

import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.model.CharacterDetails
import com.example.rickandmorty.domain.repository.CharacterLocalRepository

class InsertCharacterToDBUseCase(private val characterLocalRepository: CharacterLocalRepository) {

    suspend operator fun invoke(character: CharacterDetails){
       return characterLocalRepository.insert(character)
    }
}