package com.example.rickandmorty.data.model

import com.example.rickandmorty.data.BuildConfig
import com.example.rickandmorty.data.R

internal data class ExtraRickDTO(
    val id: Int = 1,
    val name: String = BuildConfig.RICK_NAME,
    val image: String = BuildConfig.RICK_IMG
)