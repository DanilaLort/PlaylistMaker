package com.example.playlistmaker.ui.settings.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.App
import com.example.playlistmaker.domain.api.ValueManagerRepository

class SettingViewModel(private val themeRepository: ValueManagerRepository<Boolean>) : ViewModel() {
    private var themeStateLiveData = MutableLiveData(getSavedThemeState())

    fun getThemeStateLiveData(): LiveData<Boolean> = themeStateLiveData
    private fun getSavedThemeState(): Boolean = themeRepository.getValue()
    fun saveThemeState(themeState: Boolean) {
        themeRepository.saveValue(themeState)
        if(themeStateLiveData.value != themeState) themeStateLiveData.postValue(themeState)
    }
    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val repository = (this[APPLICATION_KEY] as App).getThemeRepository()
                SettingViewModel(repository)
                }
            }
    }
}