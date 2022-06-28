package com.example.rickandmorty.data.koin

import org.koin.dsl.module

val dataModule = module {
    includes(
        networkModule,
        databaseModule,
        repositoryModule,
        useCaseModule,
        sharedPrefsModule,
        locationModule,
        mapNetworkModule
    )
}