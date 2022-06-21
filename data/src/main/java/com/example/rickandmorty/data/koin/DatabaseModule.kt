package com.example.rickandmorty.data.koin

import androidx.room.Room
import com.example.rickandmorty.data.database.CharacterDatabase
import org.koin.dsl.module

internal val databaseModule = module {
    single {
        Room.databaseBuilder(
            get(),
            CharacterDatabase::class.java,
            "characters.db"
        )
            .build()
    }

    single {
        get<CharacterDatabase>().characterDao()
    }
}