package com.example.playlistmaker.domain.usecase

import android.media.MediaPlayer
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.activity.AudioPlayerActivity

class MediaPlayerPrepareUseCase(private val url: String,
                                private val preparedListener: PreparedListener,
                                private val onCompletionListener: OnCompletionListener
) {
    fun prepareMediaPlayer() : MediaPlayer {
        var mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {preparedListener.onPrepared()
        }
        mediaPlayer.setOnCompletionListener {onCompletionListener.onCompletion()
        }
        return mediaPlayer
    }
    fun interface PreparedListener {
        fun onPrepared()
    }
    fun interface OnCompletionListener {
        fun onCompletion()
    }
}