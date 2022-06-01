package com.example.rickandmorty.domain.usecase

import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.repository.CharacterLocalRepository

class GetSomeCharactersFromDBUseCase(private val characterLocalRepository: CharacterLocalRepository) {

    suspend operator fun invoke(limit: Int = 20, offset: Int = 0): List<Character>{
        return characterLocalRepository.getCharacters(limit, offset)
    }
}