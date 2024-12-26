package com.example.playlistmaker.data.impl

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.playlistmaker.domain.api.ValueManagerRepository

class ThemeManagerRepositoryImpl(private val sharedPrefs: SharedPreferences) : ValueManagerRepository<Boolean> {
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
    }
}