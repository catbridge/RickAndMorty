package com.example.rickandmorty

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.rickandmorty.room.CharacterDatabase

class RickAndMortyApp : Application() {

    private var _database: CharacterDatabase? = null
    val database get() = requireNotNull(_database)

    override fun onCreate() {
        super.onCreate()
        _database = Room.databaseBuilder(
            this,
            CharacterDatabase::class.java,
            "ram.db"
        )
            .build()
    }
}

val Context.characterDatabase: CharacterDatabase
    get() = when (this) {
        is RickAndMortyApp -> database
        else -> applicationContext.characterDatabase
    }