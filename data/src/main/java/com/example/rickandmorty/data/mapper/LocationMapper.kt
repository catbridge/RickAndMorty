package com.example.rickandmorty.data.mapper

import com.example.rickandmorty.data.model.LocationDTO
import com.example.rickandmorty.domain.model.Location

internal fun LocationDTO.toDomainModel(): Location {
    return Location(
        id = id,
        name = name,
        type = type,
        dimension = dimension,
        residents = residents
    )
}

internal fun List<LocationDTO>.toDomainModel(): List<Location> {
    return map { it.toDomainModel() }
}