package com.example.playlistmaker.di

import com.example.playlistmaker.ui.library.view_model.FavoriteTracksViewModel
import com.example.playlistmaker.ui.library.view_model.MediaLibraryViewModel
import com.example.playlistmaker.ui.library.view_model.PlaylistFragmentViewModel
import com.example.playlistmaker.ui.main.view_model.MainActivityViewModel
import com.example.playlistmaker.ui.player.view_model.AudioPlayerViewModel
import com.example.playlistmaker.ui.search.view_model.SearchActivityViewModel
import com.example.playlistmaker.ui.settings.view_model.SettingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        SearchActivityViewModel(get(named("TrackManagerInteractor")), get())
    }
    viewModel {
        MainActivityViewModel(get(named("ThemeManagerInteractor")), get())
    }
    viewModel {
        SettingViewModel(get(named("ThemeManagerInteractor")), get())
    }
    viewModel {
        AudioPlayerViewModel(get())
    }
    viewModel {
        MediaLibraryViewModel()
    }
    viewModel {
        PlaylistFragmentViewModel()
    }
    viewModel {
        FavoriteTracksViewModel()
    }
}