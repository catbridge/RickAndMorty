package com.example.rickandmorty.domain.repository

import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.model.CharacterDetails
import com.example.rickandmorty.domain.model.Episode

interface CharacterRemoteRepository {

    suspend fun getCharacters(page: Int): List<Character>

    suspend fun getCharactersById(list: List<Int>): List<Character>

    suspend fun getCharacterDetails(id: Int): CharacterDetails

    suspend fun getEpisodeList(list: List<Int>): List<Episode>
}