package com.example.playlistmaker.di

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import com.example.playlistmaker.data.network.NetworkClient
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.network.TrackApi
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single<TrackApi> {
         Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TrackApi::class.java)
    }
    single<SharedPreferences> {
        androidContext()
            .getSharedPreferences("local_storage", Context.MODE_PRIVATE)
    }
    factory { Gson() }
    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }
    factory {
        MediaPlayer()
    }
}