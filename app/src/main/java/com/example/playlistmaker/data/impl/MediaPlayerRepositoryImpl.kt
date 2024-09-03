package com.example.playlistmaker.data.impl

import android.media.MediaPlayer
import com.example.playlistmaker.domain.repository.MediaPlayerRepository

class MediaPlayerRepositoryImpl(
        private var mediaPlayer: MediaPlayer
    ) : MediaPlayerRepository {
    private var playerState = STATE_DEFAULT
    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
    }

    override fun destroy() {
        mediaPlayer.release()
    }

    override fun getState(): Int = playerState
    override fun getCurrentPosition(): Int = mediaPlayer.currentPosition

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PLAYING = 1
        private const val STATE_PAUSED = 2
    }
}