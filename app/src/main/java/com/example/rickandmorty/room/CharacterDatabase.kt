package com.example.rickandmorty.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.rickandmorty.model.Character

@Database(entities = [Character::class], version = 1)
abstract class CharacterDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao
}