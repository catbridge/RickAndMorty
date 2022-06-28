package com.example.rickandmorty.domain.repository

import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.model.CharacterDetails

interface CharacterLocalRepository {

    suspend fun getCharacterDetails(id: Int): CharacterDetails

    suspend fun getAllCharacters(): List<Character>

    suspend fun insert(character: CharacterDetails)

    suspend fun delete(character: CharacterDetails)

    suspend fun createExtraItem(): Character
}