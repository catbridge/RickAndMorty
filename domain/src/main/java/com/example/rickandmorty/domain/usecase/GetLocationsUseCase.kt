package com.example.rickandmorty.domain.usecase

import com.example.rickandmorty.domain.model.Location
import com.example.rickandmorty.domain.repository.LocationRemoteRepository

class GetLocationsUseCase(private val locationRepository: LocationRemoteRepository) {

    suspend operator fun invoke(page: Int): List<Location> {
        return locationRepository.getLocations(page)
    }
}