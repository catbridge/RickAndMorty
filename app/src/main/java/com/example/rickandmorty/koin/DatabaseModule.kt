package com.example.rickandmorty.koin

import androidx.room.Room
import com.example.rickandmorty.room.CharacterDatabase
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            get(),
            CharacterDatabase::class.java,
            "ram.db"
        )
            .build()
    }

    single {
        get<CharacterDatabase>().characterDao()
    }
}