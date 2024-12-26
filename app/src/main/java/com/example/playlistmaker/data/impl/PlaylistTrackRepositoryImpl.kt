package com.example.playlistmaker.data.impl

import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.converters.PlaylistDbConverter
import com.example.playlistmaker.data.db.entity.PlaylistTrackEntity
import com.example.playlistmaker.domain.db.PlaylistTrackRepository
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistTrackRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistTrackDbConverter: PlaylistDbConverter
) : PlaylistTrackRepository {
    override fun getTracks(ids: List<Int>): Flow<List<Track>> = flow {
        val tracks = convertFromTrackEntity(appDatabase.playlistTrackDao().getTracks(ids))
        emit(tracks)
    }

    override suspend fun saveTrack(track: Track) {
        appDatabase.playlistTrackDao().saveTrack(playlistTrackDbConverter.map(track))
    }

    override suspend fun deleteTrack(track: Track) {
        appDatabase.playlistTrackDao().deleteTrack(playlistTrackDbConverter.map(track))
    }

    private fun convertFromTrackEntity(tracks: List<PlaylistTrackEntity>) : List<Track> {
        return tracks.map { track -> playlistTrackDbConverter.map(track) }
    }
}