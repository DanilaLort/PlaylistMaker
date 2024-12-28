package com.example.playlistmaker.ui.library.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.PlaylistInteractor
import com.example.playlistmaker.ui.playlists.PlaylistState
import kotlinx.coroutines.launch

class PlaylistFragmentViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {
    private val playlistState = MutableLiveData<PlaylistState>()
    fun getLiveDataPlaylistState(): LiveData<PlaylistState> = playlistState

    fun getPlaylists() {
        playlistState.postValue(PlaylistState.Loading)
        viewModelScope.launch {
            playlistInteractor
                .getPlaylists()
                .collect { playlists ->
                    if (playlists.isEmpty())
                        playlistState.postValue(PlaylistState.Empty)
                    else
                        playlistState.postValue(PlaylistState.Content(playlists))
                }
        }
    }
}