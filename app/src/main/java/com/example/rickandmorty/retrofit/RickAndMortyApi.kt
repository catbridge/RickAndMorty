package com.example.rickandmorty.retrofit

import com.example.rickandmorty.model.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyApi {

    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int
    ): CharacterList

    @GET("character/{id}")
    suspend fun getCharacterList(
        @Path("id") id: List<Int>
    ): List<Character>

    @GET("character/{id}")
    suspend fun getCharacterDetails(
        @Path("id") id: Int
    ): CharacterDetails

    @GET("episode/{id}")
    suspend fun getEpisode(
        @Path("id") id: Int
    ): Episode

    @GET("episode/{id}")
    suspend fun getListOfEpisode(
        @Path("id") id: List<Int>
    ): List<Episode>

    @GET("location")
    suspend fun getLocations(
        @Query("page") page : Int
    ): LocationList

    @GET("location/{id}")
    suspend fun getLocation(
        @Path("id") id: Int
    ): Location

}