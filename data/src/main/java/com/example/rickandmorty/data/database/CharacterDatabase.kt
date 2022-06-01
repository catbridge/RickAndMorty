package com.example.rickandmorty.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.rickandmorty.data.model.CharacterEntity

@Database(entities = [CharacterEntity::class], version = 1)
internal abstract class CharacterDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao
}