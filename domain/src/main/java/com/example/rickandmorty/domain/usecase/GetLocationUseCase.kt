package com.example.rickandmorty.domain.usecase

import com.example.rickandmorty.domain.model.Location
import com.example.rickandmorty.domain.repository.LocationRemoteRepository

class GetLocationUseCase(private val locationRepository: LocationRemoteRepository) {

    suspend operator fun invoke(id: Int): Result<Location> {
        return kotlin.runCatching { locationRepository.getLocation(id) }
    }
}