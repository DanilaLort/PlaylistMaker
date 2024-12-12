package com.example.playlistmaker.data.impl

import android.media.MediaPlayer
import com.example.playlistmaker.domain.api.MediaPlayerRepository

class MediaPlayerRepositoryImpl(private val mediaPlayer: MediaPlayer) : MediaPlayerRepository {
    private var playerState = STATE_DEFAULT

    override fun pausePlayer() {
        if (playerState != STATE_RELEASE) {
            mediaPlayer.pause()
            playerState = STATE_PAUSED
        }
    }
    override fun startPlayer() {
        if (playerState != STATE_RELEASE) {
            mediaPlayer.start()
            playerState = STATE_PLAYING
        }
    }
    override fun destroy() {
        playerState = STATE_RELEASE
        mediaPlayer.release()
    }

    override fun getState(): Int = playerState
    override fun setStatePrepared() {
        playerState = STATE_PREPARED
    }
    override fun setUrl(url: String) {
        if (playerState == STATE_DEFAULT) {
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
        }
    }
    override fun preparedListener(listener: () -> Unit) {
        if (playerState == STATE_DEFAULT) mediaPlayer.setOnPreparedListener { listener() }
    }

    override fun completionListener(listener: () -> Unit) {
        if (playerState == STATE_DEFAULT) mediaPlayer.setOnCompletionListener { listener() }
    }

    override fun getCurrentPosition(): Int = mediaPlayer.currentPosition

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val STATE_RELEASE = 4
    }
}