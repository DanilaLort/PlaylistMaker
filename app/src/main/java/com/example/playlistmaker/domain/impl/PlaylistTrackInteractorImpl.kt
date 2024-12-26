package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.db.PlaylistTrackInteractor
import com.example.playlistmaker.domain.db.PlaylistTrackRepository
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistTrackInteractorImpl(
    private val playlistTrackRepository: PlaylistTrackRepository
) : PlaylistTrackInteractor {
    override fun getTracks(ids: List<Int>): Flow<List<Track>> {
        return playlistTrackRepository.getTracks(ids)
    }

    override suspend fun saveTrack(track: Track) {
        playlistTrackRepository.saveTrack(track)
    }

    override suspend fun deleteTrack(track: Track) {
        playlistTrackRepository.deleteTrack(track)
    }
}