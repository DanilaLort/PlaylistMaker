package com.example.playlistmaker.di

import com.example.playlistmaker.ui.library.view_model.FavoriteTracksViewModel
import com.example.playlistmaker.ui.library.view_model.MediaLibraryViewModel
import com.example.playlistmaker.ui.library.view_model.PlaylistsFragmentViewModel
import com.example.playlistmaker.ui.main.view_model.MainActivityViewModel
import com.example.playlistmaker.ui.player.view_model.AudioPlayerViewModel
import com.example.playlistmaker.ui.playlist.view_model.PlaylistViewModel
import com.example.playlistmaker.ui.playlist_creation.view_model.PlaylistCreationViewModel
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.ui.settings.view_model.SettingViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        SearchViewModel(get(named("TrackManagerInteractor")), get())
    }
    viewModel {
        MainActivityViewModel(get(named("ThemeManagerInteractor")), get())
    }
    viewModel {
        SettingViewModel(get(named("ThemeManagerInteractor")), get())
    }
    viewModel {
        AudioPlayerViewModel(get(), get(), get())
    }
    viewModel {
        MediaLibraryViewModel()
    }
    viewModel {
        PlaylistsFragmentViewModel(get())
    }
    viewModel {
        FavoriteTracksViewModel(androidContext(), get())
    }
    viewModel {
        PlaylistCreationViewModel(get(), get())
    }
    viewModel {
        PlaylistViewModel(get())
    }
}