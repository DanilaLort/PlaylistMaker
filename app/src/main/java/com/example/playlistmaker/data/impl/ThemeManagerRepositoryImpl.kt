package com.example.playlistmaker.data.impl

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.playlistmaker.domain.api.ValueManagerRepository

class ThemeManagerRepositoryImpl(context: Context) : ValueManagerRepository<Boolean> {
    private val sharedPrefs = context.getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)

    override fun saveValue(value: Boolean) {
        sharedPrefs.edit()
            .putBoolean(KEY_FOR_THEME, value)
            .apply()
    }

    override fun getValue() : Boolean {
        return sharedPrefs.getBoolean(KEY_FOR_THEME, false)
    }
    private companion object {
        const val KEY_FOR_THEME = "key_for_theme"
        const val SHARED_PREFERENCES = "preference_for_theme"
    }
}