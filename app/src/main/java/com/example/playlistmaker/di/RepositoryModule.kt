package com.example.playlistmaker.di

import com.example.playlistmaker.App
import com.example.playlistmaker.data.db.converters.FavoriteTrackDbConverter
import com.example.playlistmaker.data.db.converters.PlaylistDbConverter
import com.example.playlistmaker.data.impl.FavoriteTracksRepositoryImpl
import com.example.playlistmaker.data.impl.MediaPlayerRepositoryImpl
import com.example.playlistmaker.data.impl.PlaylistRepositoryImpl
import com.example.playlistmaker.data.impl.ThemeManagerRepositoryImpl
import com.example.playlistmaker.data.impl.ThemeRepositoryImpl
import com.example.playlistmaker.data.impl.TrackCoverRepositoryImpl
import com.example.playlistmaker.data.impl.TrackManagerRepositoryImpl
import com.example.playlistmaker.data.impl.TracksRepositoryImpl
import com.example.playlistmaker.domain.api.MediaPlayerRepository
import com.example.playlistmaker.domain.api.ThemeRepository
import com.example.playlistmaker.domain.api.TrackCoverRepository
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.api.ValueManagerRepository
import com.example.playlistmaker.domain.db.FavoriteTracksRepository
import com.example.playlistmaker.domain.db.PlaylistRepository
import com.example.playlistmaker.domain.models.Track
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {
    factory {
        FavoriteTrackDbConverter()
    }
    factory {
        PlaylistDbConverter(get())
    }
    single<TrackCoverRepository>{
        TrackCoverRepositoryImpl(androidContext())
    }
    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get(), get())
    }
    single<FavoriteTracksRepository> {
        FavoriteTracksRepositoryImpl(get(), get())
    }
    single<ValueManagerRepository<Boolean>>(named("ThemeManagerRepository")) {
        ThemeManagerRepositoryImpl(get())
    }
    single<ValueManagerRepository<List<Track>>>(named("TrackManagerRepository")) {
        TrackManagerRepositoryImpl(get(), get())
    }
    factory<MediaPlayerRepository> {
        MediaPlayerRepositoryImpl(get())
    }
    single<TracksRepository> {
        TracksRepositoryImpl(get())
    }
    single<ThemeRepository> {
        ThemeRepositoryImpl(androidContext() as App)
    }
}