package com.example.rickandmorty.data.model

internal data class CountryDTO(
    val name: String?,
    val capital: String?,
    val latlng: List<Double>?
)