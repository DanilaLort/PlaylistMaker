package com.example.playlistmaker.data.impl

import android.app.Application
import com.example.playlistmaker.App
import com.example.playlistmaker.domain.api.ThemeRepository

class ThemeRepositoryImpl(private val application: Application) : ThemeRepository {
    override fun switchTheme(themeState: Boolean) {
        (application as App).switchTheme(themeState)
    }

}