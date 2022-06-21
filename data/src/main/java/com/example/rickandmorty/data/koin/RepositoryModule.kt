package com.example.rickandmorty.data.koin


import com.example.rickandmorty.data.repository.CharacterLocalRepositoryImpl
import com.example.rickandmorty.data.repository.CharacterRemoteRepositoryImpl
import com.example.rickandmorty.data.repository.LocationRemoteRepositoryImpl
import com.example.rickandmorty.domain.repository.CharacterLocalRepository
import com.example.rickandmorty.domain.repository.CharacterRemoteRepository
import com.example.rickandmorty.domain.repository.LocationRemoteRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val repositoryModule = module {
    singleOf(::CharacterRemoteRepositoryImpl) { bind<CharacterRemoteRepository>() }
    singleOf(::LocationRemoteRepositoryImpl) { bind<LocationRemoteRepository>() }
    singleOf(::CharacterLocalRepositoryImpl) { bind<CharacterLocalRepository>() }
}
