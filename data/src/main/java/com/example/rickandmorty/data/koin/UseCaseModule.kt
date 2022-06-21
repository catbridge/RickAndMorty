package com.example.rickandmorty.data.koin

import com.example.rickandmorty.domain.usecase.*
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val useCaseModule = module {
    factoryOf(::GetCharactersUseCase)
    factoryOf(::GetCharacterDetailsUseCase)
    factoryOf(::GetCharactersByIdUseCase)
    factoryOf(::GetEpisodesUseCase)
    factoryOf(::GetLocationUseCase)
    factoryOf(::GetLocationsUseCase)
    factoryOf(::GetExtraItemUseCase)
    factoryOf(::GetAllCharactersFromDBUseCase)
    factoryOf(::GetCharactersByNameUseCase)
    factoryOf(::InsertCharacterToDBUseCase)
    factoryOf(::DeleteCharacterFromDBUseCase)
    factoryOf(::GetCountriesUseCase)
    factoryOf(::GetCharacterDetailsFromDBUseCase)



}