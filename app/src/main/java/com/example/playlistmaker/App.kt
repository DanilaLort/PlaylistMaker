package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.api.ThemeInteractor
import com.example.playlistmaker.domain.api.ValueManagerInteractor
import com.example.playlistmaker.domain.api.ValueManagerRepository
import com.example.playlistmaker.domain.models.Track

class App : Application() {

    private var darkTheme = false

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
    fun getTrackManagerInteractor() : ValueManagerInteractor<List<Track>> = Creator.provideTrackManagerInteractor(this)
    fun getThemeManagerInteractor() : ValueManagerInteractor<Boolean> = Creator.provideThemeManagerInteractor(this)
    fun getThemeInteractor() : ThemeInteractor = Creator.provideThemeInteractor(this)
}