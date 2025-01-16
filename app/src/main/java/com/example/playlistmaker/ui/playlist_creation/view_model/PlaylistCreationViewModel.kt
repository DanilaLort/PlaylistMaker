package com.example.playlistmaker.ui.playlist_creation.view_model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api.TrackCoverInteractor
import com.example.playlistmaker.domain.db.PlaylistInteractor
import com.example.playlistmaker.domain.models.Playlist
import kotlinx.coroutines.launch

class PlaylistCreationViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val trackCoverInteractor: TrackCoverInteractor
) : ViewModel() {
    private val trackCoverLiveData = MutableLiveData<Uri>()

    fun getTrackCoverLiveData(): LiveData<Uri> = trackCoverLiveData
    fun saveTrackCover(uri: Uri) {
        val newUri = trackCoverInteractor.saveTrackCover(uri)
        trackCoverLiveData.postValue(newUri)
    }
    fun savePlaylist(playlistName: String, playlistDescription: String?, coverPath: String?) {
        val playlist = if (playlistDescription != null) Playlist(
            null,
            playlistName,
            playlistDescription,
            coverPath,
            emptyList(),
            0
        ) else Playlist(
            null,
            playlistName,
            "",
            coverPath,
            emptyList(),
            0
        )

        viewModelScope.launch {
            playlistInteractor.savePlaylist(playlist)
        }
    }

    fun updatePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.updatePlaylist(playlist)
        }
    }
}