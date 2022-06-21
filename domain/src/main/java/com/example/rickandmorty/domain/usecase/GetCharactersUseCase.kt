package com.example.rickandmorty.domain.usecase

import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.repository.CharacterRemoteRepository

class GetCharactersUseCase(private val characterRepository: CharacterRemoteRepository) {

    suspend operator fun invoke(page: Int): List<Character> {
        return characterRepository.getCharacters(page)
    }
}