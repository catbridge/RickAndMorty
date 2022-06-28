package com.example.rickandmorty.data.mapper

import com.example.rickandmorty.data.model.CharacterDTO
import com.example.rickandmorty.data.model.CharacterDetailsDTO
import com.example.rickandmorty.data.model.CharacterEntity
import com.example.rickandmorty.data.model.ExtraRickDTO
import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.model.CharacterDetails

internal fun CharacterDTO.toDomainModel(): Character {
    return Character(
        id = id,
        name = name,
        image = image,
    )
}

internal fun List<CharacterDTO>.toDomainModel(): List<Character> {
    return map { it.toDomainModel() }
}

internal fun CharacterDetailsDTO.toDomainModel(): CharacterDetails {
    return CharacterDetails(
        id = id,
        name = name,
        image = image,
        species = species,
        gender = gender,
        status = status,
        episode = episode
    )
}

internal fun CharacterDetails.toCharacterEntity(): CharacterEntity {
    return CharacterEntity(
        id = id,
        name = name,
        image = image,
        species = species,
        gender = gender,
        status = status,
        isFavourite = isFavourite
    )
}

internal fun CharacterEntity.toDomainModel(): Character {
    return Character(
        id = id,
        name = name,
        image = image
    )
}

internal fun CharacterEntity.toCharacterDetails(): CharacterDetails {
    return CharacterDetails(
        id = id,
        name = name,
        image = image,
        species = species,
        gender = gender,
        status = status,
        episode = emptyList(),
        isFavourite = isFavourite
    )
}

internal fun ExtraRickDTO.toDomainModel(): Character {
    return Character(
        id = id,
        name = name,
        image = image
    )
}