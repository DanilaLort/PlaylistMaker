package com.example.playlistmaker.ui.tracks

import com.example.playlistmaker.domain.models.Track

sealed interface TrackState {
    data object Loading : TrackState
    data object Empty : TrackState
    data class Error(val message: String) : TrackState
    data class Content(val tracks: List<Track>) : TrackState
    data class History(val tracks: List<Track>) : TrackState
}