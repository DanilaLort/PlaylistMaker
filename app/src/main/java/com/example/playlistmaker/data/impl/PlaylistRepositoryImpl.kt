package com.example.playlistmaker.data.impl

import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.converters.PlaylistDbConverter
import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.data.db.entity.PlaylistTrackEntity
import com.example.playlistmaker.domain.db.PlaylistRepository
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
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

    override suspend fun checkTrackId(id: Int): Boolean {
        val tracksId: List<List<Int>> = appDatabase.playlistDao().getPlaylistsTracksId().map { playlistDbConverter.map(it) }
        tracksId.map { if (it.contains(id)) return true }
        return false
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

    override suspend fun deleteTrack(track: Track, playlist: Playlist): Flow<Playlist> = flow {
        (playlist.trackList as ArrayList<Int>).remove(track.trackId)
        playlist.trackCountInt = playlist.trackList.size
        updatePlaylist(playlist)
        if (checkTrackId(track.trackId!!))
            appDatabase.playlistTrackDao().deleteTrack(playlistDbConverter.map(track))
        emit(playlist)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().deletePlaylist(playlistDbConverter.map(playlist))
    }

    private fun convertFromTrackEntity(tracks: List<PlaylistTrackEntity>) : List<Track> {
        return tracks.map { track -> playlistDbConverter.map(track) }
    }

    private fun convertFromEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistDbConverter.map(playlist)}
    }
}
