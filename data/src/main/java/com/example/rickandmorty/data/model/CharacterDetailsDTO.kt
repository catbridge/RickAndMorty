package com.example.rickandmorty.data.model

internal data class CharacterDetailsDTO(
    val id: Int,
    val name: String,
    val image: String,
    val species: String,
    val gender: String,
    val status: String,
    val episode: List<String>
)