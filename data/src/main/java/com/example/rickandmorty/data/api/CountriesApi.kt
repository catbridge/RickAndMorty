package com.example.rickandmorty.data.api

import com.example.rickandmorty.data.model.CountryDTO
import retrofit2.http.GET
import retrofit2.http.Url

internal interface CountriesApi {
    @GET("all")
    suspend fun getCountries(): List<CountryDTO>
}