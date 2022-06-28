package com.example.rickandmorty.domain.model



data class CharacterDetails(
    val id: Int,
    val name: String,
    val image: String,
    val species: String,
    val gender: String,
    val status: String,
    val episode: List<String>,
    var isFavourite: Boolean = false
)