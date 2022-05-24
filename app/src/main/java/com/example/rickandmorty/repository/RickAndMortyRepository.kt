package com.example.rickandmorty.repository

import com.example.rickandmorty.model.Character
import com.example.rickandmorty.model.CharacterDetails
import com.example.rickandmorty.model.Episode
import com.example.rickandmorty.model.Location
import com.example.rickandmorty.retrofit.RickAndMortyApi

class RickAndMortyRepository(private val rickAndMortyApi: RickAndMortyApi) {

    suspend fun getAllCharacters(page: Int): List<Character> {
        return rickAndMortyApi.getCharacters(page).results
    }

    suspend fun getCharactersList(list: List<Int>): List<Character>{
        return rickAndMortyApi.getCharacterList(list)
    }

    suspend fun getCharacterDetails(id: Int): CharacterDetails {
        return rickAndMortyApi.getCharacterDetails(id)
    }

    suspend fun getLocations(page: Int): List<Location>{
        return rickAndMortyApi.getLocations(page).results
    }

    suspend fun getLocation(id : Int): Location{
        return rickAndMortyApi.getLocation(id)
    }

    suspend fun getEpisode(number : Int): Episode {
        return rickAndMortyApi.getEpisode(number)
    }

    suspend fun getEpisodeList(list : List<Int>): List<Episode> {
        return rickAndMortyApi.getListOfEpisode(list)
    }
}