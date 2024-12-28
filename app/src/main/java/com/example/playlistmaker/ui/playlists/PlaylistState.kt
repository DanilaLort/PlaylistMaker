package com.example.playlistmaker.ui.playlists

import com.example.playlistmaker.domain.models.Playlist

sealed interface PlaylistState {
    data object Loading : PlaylistState
    data object Empty : PlaylistState
    data class TrackAlreadyAdded(val playlistName: String) : PlaylistState
    data class TrackAdded(val playlistName: String) : PlaylistState
    data class Content(val playlists: List<Playlist>) : PlaylistState
}