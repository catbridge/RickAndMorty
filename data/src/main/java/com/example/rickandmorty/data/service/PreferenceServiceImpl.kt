package com.example.rickandmorty.data.service

import android.content.Context
import com.example.rickandmorty.data.delegates.PrefsDelegate
import com.example.rickandmorty.domain.model.Language
import com.example.rickandmorty.domain.model.NightMode
import com.example.rickandmorty.domain.service.LanguageService
import com.example.rickandmorty.domain.service.NightModeService

class PreferenceServiceImpl (context: Context) : NightModeService, LanguageService {

    private val sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override var nightMode: NightMode by enumPref(KEY_NIGHT_MODE, NightMode.LIGHT)
    override var language: Language by enumPref(KEY_LANGUAGE_MODE, Language.EN)

    private inline fun <reified E : Enum<E>> enumPref(key: String, defaultValue: E) =
        PrefsDelegate(
            sharedPrefs,
            getValue = { getString(key, null)?.let(::enumValueOf) ?: defaultValue },
            setValue = { putString(key, it.name) }
        )

    companion object {
        private const val PREFS_NAME = "prefs"
        private const val KEY_NIGHT_MODE = "night_mode"
        private const val KEY_LANGUAGE_MODE = "lang"
    }
}