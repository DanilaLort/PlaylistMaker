package com.example.playlistmaker.domain.repository

interface MediaPlayerRepository {
    fun pausePlayer()
    fun startPlayer()
    fun destroy()
    fun getState(): Int

    fun getCurrentPosition(): Int
}