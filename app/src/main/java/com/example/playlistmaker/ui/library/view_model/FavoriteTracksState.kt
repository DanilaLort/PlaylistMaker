package com.example.playlistmaker.ui.library.view_model

import com.example.playlistmaker.domain.models.Track

sealed interface FavoriteTracksState {
    data object Loading : FavoriteTracksState

    data class Content(
        val tracks: List<Track>
    ) : FavoriteTracksState

    data class Empty(
        val message: String
    ) : FavoriteTracksState
}
