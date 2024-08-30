package com.example.playlistmaker.data.manager

import android.content.Context
import android.content.Context.MODE_PRIVATE

class ThemeManager(context: Context){
    private val sharedPrefs = context.getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)

    fun saveValue(themeMode: Boolean) {
        sharedPrefs.edit()
            .putBoolean(KEY_FOR_THEME, themeMode)
            .apply()
    }

    fun getValue() : Boolean {
        return sharedPrefs.getBoolean(KEY_FOR_THEME, false)
    }
    private companion object {
        const val KEY_FOR_THEME = "key_for_theme"
        const val SHARED_PREFERENCES = "preference_for_tracks"
    }
}