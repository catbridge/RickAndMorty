package com.example.rickandmorty.room

import androidx.room.*
import com.example.rickandmorty.model.Character
import com.example.rickandmorty.model.CharacterDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    @Query("SELECT * FROM character")
    suspend fun getCharacters(): List<Character>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(character: Character)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(characters: List<Character>)

    @Delete
    suspend fun delete(character: Character)

}