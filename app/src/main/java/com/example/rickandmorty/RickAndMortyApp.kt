package com.example.rickandmorty

import android.app.Application
import com.example.rickandmorty.koin.databaseModule
import com.example.rickandmorty.koin.networkModule
import com.example.rickandmorty.koin.repositoryModule
import com.example.rickandmorty.koin.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class RickAndMortyApp : Application() {


    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidContext(this@RickAndMortyApp)
            modules(
                databaseModule,
                networkModule,
                repositoryModule,
                viewModelModule
            )
        }

    }
}
