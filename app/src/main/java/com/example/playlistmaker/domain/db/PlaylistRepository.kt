package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    fun getPlaylistById(id: Int): Flow<Playlist>
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun savePlaylist(playlist: Playlist)
    suspend fun checkTrackId(id: Int): Boolean
    suspend fun updatePlaylist(playlist: Playlist)
    fun getTracks(ids: List<Int>): Flow<List<Track>>
    suspend fun saveTrack(track: Track)
    suspend fun deleteTrack(track: Track, playlist: Playlist): Flow<Playlist>
    suspend fun deletePlaylist(playlist: Playlist)
}