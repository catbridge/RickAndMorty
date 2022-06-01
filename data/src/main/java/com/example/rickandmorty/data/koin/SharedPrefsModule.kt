package com.example.rickandmorty.data.koin

import com.example.rickandmorty.data.service.PreferenceServiceImpl
import com.example.rickandmorty.domain.service.LanguageService
import com.example.rickandmorty.domain.service.NightModeService
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val sharedPrefsModule = module {
    singleOf(::PreferenceServiceImpl)
}