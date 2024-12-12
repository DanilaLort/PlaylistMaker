package com.example.playlistmaker.di

import com.example.playlistmaker.App
import com.example.playlistmaker.data.db.converters.TrackDbConverter
import com.example.playlistmaker.data.impl.FavoriteTracksRepositoryImpl
import com.example.playlistmaker.data.impl.MediaPlayerRepositoryImpl
import com.example.playlistmaker.data.impl.ThemeManagerRepositoryImpl
import com.example.playlistmaker.data.impl.ThemeRepositoryImpl
import com.example.playlistmaker.data.impl.TrackManagerRepositoryImpl
import com.example.playlistmaker.data.impl.TracksRepositoryImpl
import com.example.playlistmaker.domain.api.MediaPlayerRepository
import com.example.playlistmaker.domain.api.ThemeRepository
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.api.ValueManagerRepository
import com.example.playlistmaker.domain.db.FavoriteTracksRepository
import com.example.playlistmaker.domain.models.Track
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {
    single<FavoriteTracksRepository> {
        FavoriteTracksRepositoryImpl(get(), get())
    }
    factory {
        TrackDbConverter()
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