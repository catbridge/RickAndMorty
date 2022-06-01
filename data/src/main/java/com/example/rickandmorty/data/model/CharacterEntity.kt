package com.example.rickandmorty.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
internal data class CharacterEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "image")
    val image: String,
)