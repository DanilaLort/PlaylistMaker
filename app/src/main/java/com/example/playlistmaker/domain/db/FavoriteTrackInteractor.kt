package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTrackInteractor {
    fun getTracks() : Flow<List<Track>>
    fun getTrackId(): Flow<List<Int>>
    suspend fun saveTrack(track: Track)
    suspend fun deleteTrack(track: Track)
}