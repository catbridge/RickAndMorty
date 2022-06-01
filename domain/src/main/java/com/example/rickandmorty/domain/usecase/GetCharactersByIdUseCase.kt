package com.example.rickandmorty.domain.usecase

import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.repository.CharacterRemoteRepository

class GetCharactersByIdUseCase(private val characterRepository: CharacterRemoteRepository) {

    suspend operator fun invoke(list: List<Int>): List<Character> {
        return characterRepository.getCharactersById(list)
    }
}