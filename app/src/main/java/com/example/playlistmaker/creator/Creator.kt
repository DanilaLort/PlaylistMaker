package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.data.impl.TracksRepositoryImpl
import com.example.playlistmaker.data.manager.ThemeManager
import com.example.playlistmaker.data.manager.TrackManager
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    fun getTrackManager(context: Context): TrackManager {
        return TrackManager(context)
    }

    fun getThemeManager(context: Context): ThemeManager {
        return ThemeManager(context)
    }
}