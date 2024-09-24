package com.example.playlistmaker.ui.settings.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.App
import com.example.playlistmaker.domain.api.ThemeInteractor
import com.example.playlistmaker.domain.api.ValueManagerInteractor

class SettingViewModel(
    private val themeManagerInteractor: ValueManagerInteractor<Boolean>,
    private val themeInteractor: ThemeInteractor
) : ViewModel() {
    private var themeStateLiveData = MutableLiveData(getSavedThemeState())

    fun getThemeStateLiveData(): LiveData<Boolean> = themeStateLiveData
    private fun getSavedThemeState(): Boolean = themeManagerInteractor.getValue()
    fun saveThemeState(themeState: Boolean) {
        themeManagerInteractor.saveValue {
            themeState
        }
        switchTheme(themeState)
        if(themeStateLiveData.value != themeState) themeStateLiveData.postValue(themeState)
    }
    private fun switchTheme(themeState: Boolean) {
        themeInteractor.switchTheme(themeState)
    }
    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as App
                val themeManagerInteractor = application.getThemeManagerInteractor()
                val themeInteractor = application.getThemeInteractor()
                SettingViewModel(themeManagerInteractor, themeInteractor)
                }
            }
    }
}