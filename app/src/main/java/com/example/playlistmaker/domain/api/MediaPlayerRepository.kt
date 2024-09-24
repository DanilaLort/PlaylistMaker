package com.example.playlistmaker.domain.api

interface MediaPlayerRepository {
    fun pausePlayer()
    fun startPlayer()
    fun destroy()
    fun getState(): Int
    fun getCurrentPosition(): Int
    fun setStatePrepared()
    fun setUrl(url: String)
    fun preparedListener(listener: () -> Unit)
    fun completionListener(listener: () -> Unit)
}