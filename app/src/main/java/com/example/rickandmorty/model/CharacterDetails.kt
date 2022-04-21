package com.example.rickandmorty.model

data class CharacterDetails(
    val id: Int,
    val name: String,
    val image: String,
    val species: String,
    val gender: String,
    val status: String
)