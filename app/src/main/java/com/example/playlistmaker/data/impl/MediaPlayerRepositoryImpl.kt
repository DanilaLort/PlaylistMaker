package com.example.playlistmaker.data.impl

import android.media.MediaPlayer
import com.example.playlistmaker.domain.api.MediaPlayerRepository

class MediaPlayerRepositoryImpl(private val mediaPlayer: MediaPlayer) : MediaPlayerRepository {
    private var playerState = STATE_DEFAULT
//    init {
//        mediaPlayer.setDataSource(url)
//        mediaPlayer.prepareAsync()
//    }
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
    override fun setStatePrepared() {
        playerState = STATE_PREPARED
    }
    override fun setUrl(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
    }
    override fun preparedListener(listener: () -> Unit) {
        mediaPlayer.setOnPreparedListener { listener() }
    }

    override fun completionListener(listener: () -> Unit) {
        mediaPlayer.setOnCompletionListener { listener() }
    }

    override fun getCurrentPosition(): Int = mediaPlayer.currentPosition

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}