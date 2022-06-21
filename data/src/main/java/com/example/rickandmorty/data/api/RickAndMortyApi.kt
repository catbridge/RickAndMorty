package com.example.rickandmorty.data.api

import com.example.rickandmorty.data.model.*
import com.example.rickandmorty.data.model.CharacterDTO
import com.example.rickandmorty.data.model.CharacterDetailsDTO
import com.example.rickandmorty.data.model.CharacterListDTO
import com.example.rickandmorty.data.model.EpisodeDTO
import com.example.rickandmorty.data.model.LocationListDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface RickAndMortyApi {

    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int
    ): CharacterListDTO

    @GET("character/{id}")
    suspend fun getCharactersById(
        @Path("id") id: List<Int>
    ): List<CharacterDTO>

    @GET("character/{id}")
    suspend fun getCharacterDetails(
        @Path("id") id: Int
    ): CharacterDetailsDTO

    @GET("episode/{id}")
    suspend fun getEpisode(
        @Path("id") id: Int
    ): EpisodeDTO

    @GET("episode/{id}")
    suspend fun getListOfEpisode(
        @Path("id") id: List<Int>
    ): List<EpisodeDTO>

    @GET("location")
    suspend fun getLocations(
        @Query("page") page : Int
    ): LocationListDTO

    @GET("location/{id}")
    suspend fun getLocation(
        @Path("id") id: Int
    ): LocationDTO

}