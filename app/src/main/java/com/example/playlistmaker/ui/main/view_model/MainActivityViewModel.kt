package com.example.playlistmaker.ui.main.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.App
import com.example.playlistmaker.domain.api.ThemeInteractor
import com.example.playlistmaker.domain.api.ValueManagerInteractor

class MainActivityViewModel(private val themeManagerInteractor: ValueManagerInteractor<Boolean>,
                            private val themeInteractor: ThemeInteractor) : ViewModel() {
    fun updateThemeState() {
        themeInteractor.switchTheme(themeManagerInteractor.getValue())
    }
    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as App
                val themeManagerInteractor = application.getThemeManagerInteractor()
                val themeInteractor = application.getThemeInteractor()
                MainActivityViewModel(themeManagerInteractor, themeInteractor)
            }
        }
    }
}