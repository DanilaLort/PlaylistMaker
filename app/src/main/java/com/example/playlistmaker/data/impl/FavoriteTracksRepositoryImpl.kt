package com.example.playlistmaker.data.impl

import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.converters.FavoriteTrackDbConverter
import com.example.playlistmaker.data.db.entity.FavoriteTracksEntity
import com.example.playlistmaker.domain.db.FavoriteTracksRepository
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTracksRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val favoriteTrackDbConverter: FavoriteTrackDbConverter
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
        appDatabase.trackDao().insertTrack(favoriteTrackDbConverter.map(track))
    }
    override suspend fun deleteTrack(track: Track) {
        appDatabase.trackDao().deleteTrack(favoriteTrackDbConverter.map(track))
    }
    private fun convertFromTrackEntity(tracks: List<FavoriteTracksEntity>) : List<Track> {
        return tracks.map { track -> favoriteTrackDbConverter.map(track) }
    }
}