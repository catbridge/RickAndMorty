package com.example.rickandmorty.domain.usecase

import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.repository.CharacterLocalRepository

class GetAllCharactersFromDBUseCase(private val characterLocalRepository: CharacterLocalRepository) {

    suspend operator fun invoke(): Result<List<Character>>{
        return runCatching { characterLocalRepository.getAllCharacters() }
    }
}