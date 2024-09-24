package com.example.playlistmaker.domain.api

interface MediaPlayerInteractor {
    fun start(consumer: MediaPlayerConsumer)
    fun pause(consumer: MediaPlayerConsumer)
    fun destroy()
    fun getCurrentPosition(): String
    fun getState(): Int
    fun setStatePrepared()
    fun setUrl(url: String)
    fun preparedListener(consumer: MediaPlayerConsumer)
    fun completionListener(consumer: MediaPlayerConsumer)
    fun interface MediaPlayerConsumer {
        fun consume()
    }
}