package com.example.rickandmorty.domain.repository

import com.example.rickandmorty.domain.model.Location

interface LocationRemoteRepository {

    suspend fun getLocations(page: Int): List<Location>

    suspend fun getLocation(id: Int): Location

}