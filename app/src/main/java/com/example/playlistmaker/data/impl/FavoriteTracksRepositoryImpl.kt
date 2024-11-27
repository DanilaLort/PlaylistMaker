package com.example.playlistmaker.data.impl

import android.util.Log
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.converters.TrackDbConverter
import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.domain.db.FavoriteTracksRepository
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTracksRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConverter: TrackDbConverter
) : FavoriteTracksRepository {
    override fun getTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getTracks()
        emit(convertFromTrackEntity(tracks))
    }
    override fun getTracksId(): Flow<List<Int>> = flow {
        val tracksId = appDatabase.trackDao().getTracksId()
        emit(tracksId)
    }
    override suspend fun saveFavoriteTrack(track: Track) {
        appDatabase.trackDao().insertTrack(trackDbConverter.map(track))
    }
    override suspend fun deleteTrack(track: Track) {
        appDatabase.trackDao().deleteTrack(trackDbConverter.map(track))
    }
    private fun convertFromTrackEntity(tracks: List<TrackEntity>) : List<Track> {
        return tracks.map { track -> trackDbConverter.map(track) }
    }
}