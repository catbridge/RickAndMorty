package com.example.rickandmorty.data.database

import androidx.room.*
import com.example.rickandmorty.data.model.CharacterEntity

@Dao
internal interface CharacterDao {

    @Query("SELECT * FROM characterentity")
    suspend fun getAllCharacters(): List<CharacterEntity>

    @Query("SELECT * FROM characterentity WHERE id = :id")
    suspend fun getCharacterDetails(id: Int): CharacterEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(character: CharacterEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(characters: List<CharacterEntity>)

    @Delete()
    suspend fun delete(character: CharacterEntity)

}