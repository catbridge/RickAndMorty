package com.example.rickandmorty

import android.app.Application
import android.content.Context
import com.example.rickandmorty.data.koin.dataModule
import com.example.rickandmorty.data.service.PreferenceServiceImpl
import com.example.rickandmorty.extensions.applySelectedAppLanguage
import com.example.rickandmorty.koin.viewModelModule
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class RickAndMortyApp : Application() {

//    private val prefsManager: PreferenceServiceImpl by inject()
//
//    override fun attachBaseContext(base: Context) {
//        super.attachBaseContext(base.applySelectedAppLanguage(prefsManager.language))
//    }

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
