package com.example.rickandmorty.domain.usecase

import com.example.rickandmorty.domain.model.Country
import com.example.rickandmorty.domain.repository.CountryRemoteRepository

class GetCountriesUseCase(private val countryRemoteRepository: CountryRemoteRepository) {

    suspend operator fun invoke(): Result<List<Country>> {
        return runCatching { countryRemoteRepository.getCountries() }
    }
}