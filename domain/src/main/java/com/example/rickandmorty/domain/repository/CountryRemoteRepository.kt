package com.example.rickandmorty.domain.repository

import com.example.rickandmorty.domain.model.Country

interface CountryRemoteRepository {

    suspend fun getCountries(): List<Country>

}