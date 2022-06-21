package com.example.rickandmorty.data.repository

import com.example.rickandmorty.data.api.RickAndMortyApi
import com.example.rickandmorty.data.mapper.toDomainModel
import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.model.CharacterDetails
import com.example.rickandmorty.domain.model.Episode
import com.example.rickandmorty.domain.repository.CharacterRemoteRepository
import java.util.concurrent.Flow

internal class CharacterRemoteRepositoryImpl(private val api: RickAndMortyApi) :
    CharacterRemoteRepository {

    override suspend fun getCharacters(page: Int): List<Character> {
        return api.getCharacters(page).results.toDomainModel()
    }

    override suspend fun getCharactersById(list: List<Int>): List<Character> {
        return api.getCharactersById(list).toDomainModel()
    }

    override suspend fun getCharacterDetails(id: Int): CharacterDetails {
        return api.getCharacterDetails(id).toDomainModel()
    }

    override suspend fun getEpisodeList(list: List<Int>): List<Episode> {
        return api.getListOfEpisode(list).toDomainModel()
    }

    override suspend fun getCharactersByName(page: Int, name: String): List<Character> {
        return api.getCharactersByName(page = page, name = name).results.toDomainModel()
    }
}