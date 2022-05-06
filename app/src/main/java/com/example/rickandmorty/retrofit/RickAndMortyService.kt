package com.example.rickandmorty.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RickAndMortyService {

    private val retrofit by lazy(LazyThreadSafetyMode.NONE) { provideRetrofit() }
    val rickAndMortyApi by lazy(LazyThreadSafetyMode.NONE) {
        retrofit.create<RickAndMortyApi>()
    }

    private fun provideRetrofit(): Retrofit {
        val client = OkHttpClient.Builder()
            .build()

        return Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
}