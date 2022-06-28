package com.example.rickandmorty.data.koin

import com.example.rickandmorty.data.service.LocationService
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val locationModule = module {
        singleOf(::LocationService)
}
