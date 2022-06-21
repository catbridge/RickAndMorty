package com.example.rickandmorty.domain.usecase

import com.example.rickandmorty.domain.model.Episode
import com.example.rickandmorty.domain.repository.CharacterRemoteRepository

class GetEpisodesUseCase(private val characterRepository: CharacterRemoteRepository) {

    suspend operator fun invoke(list: List<Int>): List<Episode> {
        return characterRepository.getEpisodeList(list)
    }
}