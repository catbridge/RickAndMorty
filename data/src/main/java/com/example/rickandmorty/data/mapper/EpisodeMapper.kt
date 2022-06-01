package com.example.rickandmorty.data.mapper

import com.example.rickandmorty.data.model.EpisodeDTO
import com.example.rickandmorty.domain.model.Episode

internal fun EpisodeDTO.toDomainModel(): Episode {
    return Episode(
        id = id,
        name = name,
        episode = episode
    )
}

internal fun List<EpisodeDTO>.toDomainModel(): List<Episode> {
    return map { it.toDomainModel() }
}