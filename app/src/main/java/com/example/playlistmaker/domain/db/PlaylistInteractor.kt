package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun savePlaylist(playlist: Playlist)
    suspend fun updatePlaylist(track: Track, playlist: Playlist): Boolean
    fun getTracks(ids: List<Int>): Flow<List<Track>>
    suspend fun saveTrack(track: Track)
    suspend fun deleteTrack(track: Track)
}