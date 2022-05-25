package com.example.rickandmorty.koin

import com.example.rickandmorty.repository.RickAndMortyRepository
import com.example.rickandmorty.retrofit.RickAndMortyApi
import org.koin.dsl.module

val repositoryModule = module {
    single { RickAndMortyRepository(get()) }
}
