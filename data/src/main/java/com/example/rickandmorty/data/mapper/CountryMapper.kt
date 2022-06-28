package com.example.rickandmorty.data.mapper

import com.example.rickandmorty.data.model.CountryDTO
import com.example.rickandmorty.domain.model.Country

internal fun CountryDTO.toDomainModel(): Country {
    return Country(
        name = name,
        latlng = latlng,
        capital = capital
    )
}

internal fun List<CountryDTO>.toDomainModel(): List<Country>{
    return map { it.toDomainModel() }
}