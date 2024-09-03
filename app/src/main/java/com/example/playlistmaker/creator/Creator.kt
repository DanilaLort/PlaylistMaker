package com.example.playlistmaker.creator

import android.content.Context
import android.media.MediaPlayer
import com.example.playlistmaker.data.impl.MediaPlayerRepositoryImpl
import com.example.playlistmaker.data.impl.ThemeManagerRepositoryImpl
import com.example.playlistmaker.data.impl.TrackManagerRepositoryImpl
import com.example.playlistmaker.data.impl.TracksRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.impl.MediaPlayerInteractorImpl
import com.example.playlistmaker.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.impl.ValueManagerInteractorImpl
import com.example.playlistmaker.domain.interactor.MediaPlayerInteractor
import com.example.playlistmaker.domain.interactor.TracksInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.MediaPlayerRepository
import com.example.playlistmaker.domain.repository.TracksRepository

object Creator {
    private fun getTracksRepository() : TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    private fun getTrackManagerRepository(context: Context) : TrackManagerRepositoryImpl {
        return TrackManagerRepositoryImpl(context)
    }

    private fun getThemeManagerRepository(context: Context) : ThemeManagerRepositoryImpl {
        return ThemeManagerRepositoryImpl(context)
    }

    private fun getMediaPlayerRepository(mediaPlayer: MediaPlayer) : MediaPlayerRepository {
        return MediaPlayerRepositoryImpl(mediaPlayer)
    }

    fun provideMediaPlayerInteractor(mediaPlayer: MediaPlayer) : MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(getMediaPlayerRepository(mediaPlayer))
    }

    fun provideTracksInteractor() : TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    fun provideTrackManagerInteractor(context: Context) : ValueManagerInteractorImpl<List<Track>> {
        return ValueManagerInteractorImpl(getTrackManagerRepository(context))
    }

    fun provideThemeManagerInteractor(context: Context) : ValueManagerInteractorImpl<Boolean> {
        return ValueManagerInteractorImpl(getThemeManagerRepository(context))
    }
}