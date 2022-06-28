package com.example.rickandmorty

import android.app.Application
import com.example.rickandmorty.data.koin.dataModule
import com.example.rickandmorty.koin.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class RickAndMortyApp : Application() {


    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@RickAndMortyApp)
            modules(
                dataModule,
                viewModelModule
            )
        }

    }
}
