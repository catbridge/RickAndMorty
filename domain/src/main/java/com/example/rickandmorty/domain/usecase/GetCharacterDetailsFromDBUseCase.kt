package com.example.rickandmorty.domain.usecase

import com.example.rickandmorty.domain.model.CharacterDetails
import com.example.rickandmorty.domain.repository.CharacterLocalRepository

class GetCharacterDetailsFromDBUseCase(private val repository: CharacterLocalRepository) {

    suspend operator fun invoke(id: Int): Result<CharacterDetails> {
       return runCatching { repository.getCharacterDetails(id) }
    }
}