package com.example.rickandmorty

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.rickandmorty.domain.model.NightMode
import com.example.rickandmorty.domain.service.PrefsService
import com.example.rickandmorty.extensions.applySelectedAppLanguage
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val prefsManager: PrefsService by inject()

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(newBase.applySelectedAppLanguage(prefsManager.language))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        AppCompatDelegate.setDefaultNightMode(
            when (prefsManager.nightMode) {
                NightMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
                NightMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}