package com.example.playlistmaker.ui.search.view_model

import com.example.playlistmaker.domain.models.Track

sealed interface TrackState {
    data object Loading : TrackState
    data object Empty : TrackState
    data class Error(val message: String) : TrackState
    data class Content(val tracks: List<Track>) : TrackState
    data class History(val tracks: List<Track>) : TrackState
}