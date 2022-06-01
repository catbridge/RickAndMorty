package com.example.rickandmorty.data.repository

import com.example.rickandmorty.data.api.RickAndMortyApi
import com.example.rickandmorty.data.mapper.toDomainModel
import com.example.rickandmorty.domain.model.Location
import com.example.rickandmorty.domain.repository.LocationRemoteRepository

internal class LocationRemoteRepositoryImpl(private val api: RickAndMortyApi) :
    LocationRemoteRepository {

    override suspend fun getLocations(page: Int): List<Location> {
        return api.getLocations(page).results.toDomainModel()
    }

    override suspend fun getLocation(id: Int): Location {
        return api.getLocation(id).toDomainModel()
    }
}