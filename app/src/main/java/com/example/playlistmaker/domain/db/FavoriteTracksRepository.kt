package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksRepository {
    fun getTracks() : Flow<List<Track>>
    fun getTracksId() : Flow<List<Int>>
    suspend fun deleteTrack(track: Track)
    suspend fun saveFavoriteTrack(track: Track)
}