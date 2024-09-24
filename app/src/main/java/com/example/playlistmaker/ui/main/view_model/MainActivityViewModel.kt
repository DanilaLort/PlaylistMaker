package com.example.playlistmaker.ui.main.view_model

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.api.ThemeInteractor
import com.example.playlistmaker.domain.api.ValueManagerInteractor

class MainActivityViewModel(private val themeManagerInteractor: ValueManagerInteractor<Boolean>,
                            private val themeInteractor: ThemeInteractor) : ViewModel() {
    fun updateThemeState() {
        themeInteractor.switchTheme(themeManagerInteractor.getValue())
    }
}