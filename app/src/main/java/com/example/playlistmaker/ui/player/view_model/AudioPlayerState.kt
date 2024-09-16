package com.example.playlistmaker.ui.player.view_model

sealed interface AudioPlayerState {
    data object Prepared : AudioPlayerState
    data object Completion : AudioPlayerState
    data object Start : AudioPlayerState
    data object Pause : AudioPlayerState
    data class Playing(val timer: String) : AudioPlayerState
}