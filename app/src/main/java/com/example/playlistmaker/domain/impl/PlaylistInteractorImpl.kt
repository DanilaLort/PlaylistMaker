package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.db.PlaylistInteractor
import com.example.playlistmaker.domain.db.PlaylistRepository
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository
) : PlaylistInteractor {
    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getPlaylists()
    }

    override suspend fun savePlaylist(playlist: Playlist) {
        playlistRepository.savePlaylist(playlist)
    }

    override suspend fun updatePlaylist(track: Track, playlist: Playlist): Boolean {
        if (!playlist.trackList.contains(track.trackId)) {
            (playlist.trackList as ArrayList).add(track.trackId!!)
            playlist.trackCount = playlist.trackList.size
            playlistRepository.saveTrack(track)
            playlistRepository.updatePlaylist(playlist)
            return true
        }
        return false
    }

    override fun getTracks(ids: List<Int>): Flow<List<Track>> {
        return playlistRepository.getTracks(ids)
    }

    override suspend fun saveTrack(track: Track) {
        playlistRepository.saveTrack(track)
    }

    override suspend fun deleteTrack(track: Track) {
        playlistRepository.deleteTrack(track)
    }
}