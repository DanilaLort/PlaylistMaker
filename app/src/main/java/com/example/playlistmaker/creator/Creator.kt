package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.data.impl.MediaPlayerRepositoryImpl
import com.example.playlistmaker.data.impl.ThemeManagerRepositoryImpl
import com.example.playlistmaker.data.impl.TrackManagerRepositoryImpl
import com.example.playlistmaker.data.impl.TracksRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.domain.api.MediaPlayerRepository
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.api.ValueManagerRepository
import com.example.playlistmaker.domain.impl.MediaPlayerInteractorImpl
import com.example.playlistmaker.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.models.Track

object Creator {
    private fun getTracksRepository() : TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun getTrackManagerRepository(context: Context) : ValueManagerRepository<List<Track>> {
        return TrackManagerRepositoryImpl(context)
    }

    fun getThemeManagerRepository(context: Context) : ValueManagerRepository<Boolean> {
        return ThemeManagerRepositoryImpl(context)
    }

    private fun getMediaPlayerRepository(url: String) : MediaPlayerRepository {
        return MediaPlayerRepositoryImpl(url)
    }

    fun provideMediaPlayerInteractor(url: String) : MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(getMediaPlayerRepository(url))
    }

    fun provideTracksInteractor() : TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }
}