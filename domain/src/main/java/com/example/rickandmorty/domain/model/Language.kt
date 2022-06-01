package com.example.rickandmorty.domain.model

import java.util.*

enum class Language(val locale: Locale) {
    EN(locale = Locale.ENGLISH),
    RU(locale = Locale("ru"))
}