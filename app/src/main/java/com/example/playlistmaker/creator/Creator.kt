package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.data.impl.MediaPlayerRepositoryImpl
import com.example.playlistmaker.data.impl.ThemeManagerRepositoryImpl
import com.example.playlistmaker.data.impl.TrackManagerRepositoryImpl
import com.example.playlistmaker.data.impl.TracksRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.impl.MediaPlayerInteractorImpl
import com.example.playlistmaker.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.impl.ValueManagerInteractorImpl
import com.example.playlistmaker.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.ValueManagerInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.api.MediaPlayerRepository
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.api.ValueManagerRepository

object Creator {
    private fun getTracksRepository() : TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    private fun getTrackManagerRepository(context: Context) : ValueManagerRepository<List<Track>> {
        return TrackManagerRepositoryImpl(context)
    }

    private fun getThemeManagerRepository(context: Context) : ValueManagerRepository<Boolean> {
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

    fun provideTrackManagerInteractor(context: Context) : ValueManagerInteractor<List<Track>> {
        return ValueManagerInteractorImpl(getTrackManagerRepository(context))
    }

    fun provideThemeManagerInteractor(context: Context) : ValueManagerInteractor<Boolean> {
        return ValueManagerInteractorImpl(getThemeManagerRepository(context))
    }
}