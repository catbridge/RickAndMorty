package com.example.rickandmorty.repository

import com.example.rickandmorty.model.CharacterDetails
import com.example.rickandmorty.model.CharacterList
import com.example.rickandmorty.provider.ServiceProvider
import com.example.rickandmorty.retrofit.RickAndMortyService

class RickAndMortyRepository {

    suspend fun getCharacters(pages: Int): CharacterList {
        return RickAndMortyService.rickAndMortyApi.getCharacters(pages)
    }

    suspend fun getCharacterDetails(id: Int): CharacterDetails {
        return RickAndMortyService.rickAndMortyApi.getCharacterDetails(id)
    }
}