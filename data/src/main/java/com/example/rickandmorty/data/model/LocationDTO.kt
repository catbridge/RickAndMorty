package com.example.rickandmorty.data.model

internal data class LocationDTO(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    val residents: List<String>
)