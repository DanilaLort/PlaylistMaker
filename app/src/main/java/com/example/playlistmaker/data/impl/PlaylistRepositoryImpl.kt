package com.example.playlistmaker.data.impl

import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.converters.PlaylistDbConverter
import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.data.db.entity.PlaylistTrackEntity
import com.example.playlistmaker.domain.db.PlaylistRepository
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConverter: PlaylistDbConverter
) : PlaylistRepository {
    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        appDatabase.playlistDao().getPlaylists().collect {
            val playlists = convertFromEntity(it)
            emit(playlists)
        }
    }

    override suspend fun savePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().savePlaylist(playlistDbConverter.map(playlist))
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().updatePlaylist(playlistDbConverter.map(playlist))
    }


    override fun getTracks(ids: List<Int>): Flow<List<Track>> = flow {
        appDatabase.playlistTrackDao().getTracks(ids).collect {
            val tracks = convertFromTrackEntity(it)
            emit(tracks)
        }
    }

    override suspend fun saveTrack(track: Track) {
        appDatabase.playlistTrackDao().saveTrack(playlistDbConverter.map(track))
    }

    override suspend fun deleteTrack(track: Track) {
        appDatabase.playlistTrackDao().deleteTrack(playlistDbConverter.map(track))
    }

    private fun convertFromTrackEntity(tracks: List<PlaylistTrackEntity>) : List<Track> {
        return tracks.map { track -> playlistDbConverter.map(track) }
    }

    private fun convertFromEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistDbConverter.map(playlist)}
    }
}
