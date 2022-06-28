package com.example.rickandmorty.data.koin

import com.example.rickandmorty.data.BuildConfig
import com.example.rickandmorty.data.api.CountriesApi
import com.example.rickandmorty.domain.model.NetworkQualifier
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

val mapNetworkModule = module {

    single(qualifier(NetworkQualifier.MAP)) {
        Retrofit.Builder()
            .baseUrl(BuildConfig.MAP_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
    }

    single { get<Retrofit>((qualifier(NetworkQualifier.MAP))).create<CountriesApi>() }
}