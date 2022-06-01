package com.example.rickandmorty.domain.repository

import com.example.rickandmorty.domain.model.Character

interface CharacterLocalRepository {
    suspend fun getCharacters(limit: Int, offset: Int): List<Character>

    suspend fun getAllCharacters(): List<Character>

    suspend fun insert(character: Character)

    suspend fun insertList(characters: List<Character>)

    suspend fun delete(character: Character)

    suspend fun createExtraItem(): Character
}