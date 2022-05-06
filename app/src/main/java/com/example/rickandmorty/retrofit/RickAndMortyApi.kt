package com.example.rickandmorty.retrofit

import com.example.rickandmorty.model.CharacterDetails
import com.example.rickandmorty.model.CharacterList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyApi {

    @GET("character")
    fun getCharacters(
        @Query("page") page : Int
    ): Call<CharacterList>

    @GET("character/{id}")
    fun getCharacterDetails(
        @Path("id") id: Int
    ): Call<CharacterDetails>
}