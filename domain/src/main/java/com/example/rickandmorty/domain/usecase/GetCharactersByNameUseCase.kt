package com.example.rickandmorty.domain.usecase

import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.repository.CharacterRemoteRepository

class GetCharactersByNameUseCase(private val characterRepository: CharacterRemoteRepository) {

    suspend operator fun invoke(page: Int, name: String): Result<List<Character>> {
        return runCatching { characterRepository.getCharactersByName(page, name) }
    }
}