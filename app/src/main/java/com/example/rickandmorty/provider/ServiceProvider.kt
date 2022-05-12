package com.example.rickandmorty.provider

import com.example.rickandmorty.retrofit.RickAndMortyApi
import com.example.rickandmorty.retrofit.RickAndMortyService

object ServiceProvider {

    private val rickAndMortyService = RickAndMortyService

    fun provideRickAndMortyApi(): RickAndMortyApi {
        return rickAndMortyService.rickAndMortyApi
    }
}