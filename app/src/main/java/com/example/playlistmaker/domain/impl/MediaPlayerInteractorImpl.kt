package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.interactor.MediaPlayerInteractor
import com.example.playlistmaker.domain.repository.MediaPlayerRepository
import java.text.SimpleDateFormat
import java.util.Locale

class MediaPlayerInteractorImpl (
        private val mediaPlayerRepository: MediaPlayerRepository
    ) : MediaPlayerInteractor {
    override fun start(consumer: MediaPlayerInteractor.MediaPlayerConsumer) {
        mediaPlayerRepository.startPlayer()
        consumer.consume()
    }

    override fun pause(consumer: MediaPlayerInteractor.MediaPlayerConsumer) {
        mediaPlayerRepository.pausePlayer()
        consumer.consume()
    }

    override fun destroy() {
        mediaPlayerRepository.destroy()
    }

    override fun getCurrentPosition(): String {
        return SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()).format(mediaPlayerRepository.getCurrentPosition())
    }

    override fun getState(): Int {
        return mediaPlayerRepository.getState()
    }

}