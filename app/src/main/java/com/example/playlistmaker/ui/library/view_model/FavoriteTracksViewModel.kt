package com.example.playlistmaker.ui.library.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.db.FavoriteTrackInteractor
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(
    private val context: Context,
    private val favoriteTracksInteractor: FavoriteTrackInteractor
) : ViewModel() {
    private val stateLiveData = MutableLiveData<FavoriteTracksState>()
    fun observeState(): LiveData<FavoriteTracksState> = stateLiveData
    fun fillData() {
        renderState(FavoriteTracksState.Loading)
        viewModelScope.launch {
            favoriteTracksInteractor
                .getTracks()
                .collect { tracks ->
                    processResult(tracks)
                }
        }
    }
    private fun processResult(tracks: List<Track>) {
        if (tracks.isEmpty())
            renderState(FavoriteTracksState.Empty(context.getString(R.string.emptyMedialib)))
        else
            renderState(FavoriteTracksState.Content(tracks))
    }
    private fun renderState(state: FavoriteTracksState) {
        stateLiveData.postValue(state)
    }
}