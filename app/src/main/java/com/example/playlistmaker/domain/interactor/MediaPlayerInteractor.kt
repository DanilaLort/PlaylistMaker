package com.example.playlistmaker.domain.interactor

interface MediaPlayerInteractor {
    fun start(consumer: MediaPlayerConsumer)
    fun pause(consumer: MediaPlayerConsumer)
    fun destroy()
    fun getCurrentPosition(): String
    fun getState(): Int
    interface MediaPlayerConsumer {
        fun consume()
    }
}