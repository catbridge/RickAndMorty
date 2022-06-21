package com.example.rickandmorty.domain.usecase

import com.example.rickandmorty.domain.model.CharacterDetails
import com.example.rickandmorty.domain.repository.CharacterRemoteRepository

class GetCharacterDetailsUseCase(private val characterRepository: CharacterRemoteRepository) {

    suspend operator fun invoke(id: Int): Result<CharacterDetails> {
        return runCatching { characterRepository.getCharacterDetails(id) }
    }
}