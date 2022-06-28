package com.example.rickandmorty.data.repository

import com.example.rickandmorty.data.api.CountriesApi
import com.example.rickandmorty.data.api.RickAndMortyApi
import com.example.rickandmorty.data.mapper.toDomainModel
import com.example.rickandmorty.domain.model.Country
import com.example.rickandmorty.domain.repository.CountryRemoteRepository

internal class CountryRemoteRepositoryImpl(private val api: CountriesApi): CountryRemoteRepository {

    override suspend fun getCountries(): List<Country> {
        return api.getCountries().toDomainModel()
    }
}