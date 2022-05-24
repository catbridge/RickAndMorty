package com.example.rickandmorty.room

import androidx.room.*
import com.example.rickandmorty.model.Character

@Dao
interface CharacterDao {

    @Query("SELECT * FROM character LIMIT :limit OFFSET :offset")
    suspend fun getCharacters(limit: Int, offset: Int): List<Character>

    @Query("SELECT * FROM character")
    suspend fun getAllCharacters(): List<Character>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(character: Character)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(characters: List<Character>)

    @Delete
    suspend fun delete(character: Character)

}