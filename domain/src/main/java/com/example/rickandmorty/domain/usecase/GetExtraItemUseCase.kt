package com.example.rickandmorty.domain.usecase

import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.repository.CharacterLocalRepository

class GetExtraItemUseCase(private val characterRepository: CharacterLocalRepository) {

    suspend operator fun invoke(): Character {
        return characterRepository.createExtraItem()
    }
}