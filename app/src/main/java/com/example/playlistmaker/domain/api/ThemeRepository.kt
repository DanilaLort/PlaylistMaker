package com.example.playlistmaker.domain.api

interface ThemeRepository {
    fun switchTheme(themeState: Boolean)
}