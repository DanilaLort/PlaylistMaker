package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun savePlaylist(playlist: Playlist)
    suspend fun updatePlaylist(playlist: Playlist)
}