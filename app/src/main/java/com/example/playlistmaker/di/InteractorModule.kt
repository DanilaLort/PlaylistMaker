package com.example.playlistmaker.di

import com.example.playlistmaker.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.domain.api.ThemeInteractor
import com.example.playlistmaker.domain.api.TrackCoverInteractor
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.ValueManagerInteractor
import com.example.playlistmaker.domain.db.FavoriteTrackInteractor
import com.example.playlistmaker.domain.db.PlaylistInteractor
import com.example.playlistmaker.domain.impl.FavoriteTrackInteractorImpl
import com.example.playlistmaker.domain.impl.MediaPlayerInteractorImpl
import com.example.playlistmaker.domain.impl.PlaylistInteractorImpl
import com.example.playlistmaker.domain.impl.ThemeInteractorImpl
import com.example.playlistmaker.domain.impl.TrackCoverInteractorImpl
import com.example.playlistmaker.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.impl.ValueManagerInteractorImpl
import com.example.playlistmaker.domain.models.Track
import org.koin.core.qualifier.named
import org.koin.dsl.module

val interactorModule = module {
    factory<MediaPlayerInteractor> {
        MediaPlayerInteractorImpl(get())
    }
    single<TrackCoverInteractor> {
        TrackCoverInteractorImpl(get())
    }
    single<PlaylistInteractor> {
        PlaylistInteractorImpl(get())
    }
    single<FavoriteTrackInteractor> {
        FavoriteTrackInteractorImpl(get())
    }
    single<TracksInteractor> {
        TracksInteractorImpl(get())
    }
    single<ValueManagerInteractor<Boolean>>(named("ThemeManagerInteractor")) {
        ValueManagerInteractorImpl(get(named("ThemeManagerRepository")))
    }
    single<ValueManagerInteractor<List<Track>>>(named("TrackManagerInteractor")) {
        ValueManagerInteractorImpl(get(named("TrackManagerRepository")))
    }
    single<ThemeInteractor> {
        ThemeInteractorImpl(get())
    }
}