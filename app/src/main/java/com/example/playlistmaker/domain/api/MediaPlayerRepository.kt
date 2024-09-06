package com.example.playlistmaker.domain.api

interface MediaPlayerRepository {
    fun pausePlayer()
    fun startPlayer()
    fun destroy()
    fun getState(): Int
    fun setStatePrepared()
    fun preparedListener(listener: () -> Unit)
    fun completionListener(listener: () -> Unit)
    fun getCurrentPosition(): Int
}