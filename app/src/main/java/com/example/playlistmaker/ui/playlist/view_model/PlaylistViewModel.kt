package com.example.playlistmaker.ui.playlist.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.PlaylistInteractor
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.tracks.TrackState
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {
    private val playlistTracksLiveData: MutableLiveData<TrackState> = MutableLiveData<TrackState>()
    private val playlistLiveData: MutableLiveData<Playlist> = MutableLiveData<Playlist>()


    fun getPlaylistTrackLiveData(): LiveData<TrackState> = playlistTracksLiveData
    fun getPlaylistLiveData(): LiveData<Playlist> = playlistLiveData

    fun deleteTrack(track: Track, playlist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.deleteTrack(track, playlist).collect {
                playlistLiveData.postValue(it)
            }
        }
    }

    fun deletePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.deletePlaylist(playlist)
        }
    }

    fun getTracks(ids: List<Int>) {
        viewModelScope.launch {
            playlistInteractor.getTracks(ids).collect { tracks ->
                playlistTracksLiveData.postValue(TrackState.Content(tracks))
            }
        }
    }

}