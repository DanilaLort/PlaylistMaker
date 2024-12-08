package com.example.playlistmaker.ui.settings.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
        if(themeStateLiveData.value != themeState) {
            switchTheme(themeState)
        }
    }
    private fun switchTheme(themeState: Boolean) {
        themeInteractor.switchTheme(themeState)
        themeStateLiveData.postValue(themeState)
    }
}
