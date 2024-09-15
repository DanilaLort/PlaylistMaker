package com.example.playlistmaker.ui.main.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.App
import com.example.playlistmaker.domain.api.ValueManagerRepository

class MainActivityViewModel(private val themeRepository: ValueManagerRepository<Boolean>) : ViewModel() {
    private var themeStateLiveData = MutableLiveData(themeRepository.getValue())

    fun getThemeStateLiveData(): LiveData<Boolean> = themeStateLiveData
    fun updateThemeState() {
        themeStateLiveData.postValue(themeRepository.getValue())
    }
    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val repository = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as App).getThemeRepository()
                MainActivityViewModel(repository)
            }
        }
    }
}