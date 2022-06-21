package com.example.rickandmorty.data.koin

import com.example.rickandmorty.data.BuildConfig
import com.example.rickandmorty.data.api.RickAndMortyApi
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

val networkModule = module {

    single {
        OkHttpClient.Builder().build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.RAM_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
    }

    single { get<Retrofit>().create<RickAndMortyApi>() }
}

