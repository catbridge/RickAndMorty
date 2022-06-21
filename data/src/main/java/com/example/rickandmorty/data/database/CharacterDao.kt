package com.example.rickandmorty.data.database

import androidx.room.*
import com.example.rickandmorty.data.model.CharacterEntity

@Dao
internal interface CharacterDao {

    @Query("SELECT * FROM characterentity LIMIT :limit OFFSET :offset")
    suspend fun getCharacters(limit: Int, offset: Int): List<CharacterEntity>

    @Query("SELECT * FROM characterentity")
    suspend fun getAllCharacters(): List<CharacterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(character: CharacterEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(characters: List<CharacterEntity>)

    @Delete
    suspend fun delete(character: CharacterEntity)

}