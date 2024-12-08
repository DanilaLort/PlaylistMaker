package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.db.FavoriteTrackInteractor
import com.example.playlistmaker.domain.db.FavoriteTracksRepository
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoriteTrackInteractorImpl(
    private val favoriteTracksRepository: FavoriteTracksRepository
) : FavoriteTrackInteractor {
    override fun getTracks(): Flow<List<Track>> {
        return favoriteTracksRepository.getTracks()
    }
    override suspend fun saveTrack(track: Track) {
        favoriteTracksRepository.saveFavoriteTrack(track)
    }

    override suspend fun deleteTrack(track: Track) {
        favoriteTracksRepository.deleteTrack(track)
    }

    override fun getTrackId(): Flow<List<Int>> {
        return favoriteTracksRepository.getTracksId()
    }

}