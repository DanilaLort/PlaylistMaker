package com.example.playlistmaker.data.db.converters

import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.data.db.entity.PlaylistTrackEntity
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Calendar

class PlaylistDbConverter(private val gson: Gson) {

    private val trackListType = object : TypeToken<List<Int>>() {}.type
    fun map(track: Track): PlaylistTrackEntity {
        return PlaylistTrackEntity(track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.trackId,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            Calendar.getInstance().time.time)
    }

    fun map(track: PlaylistTrackEntity): Track {
        return Track(
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.id,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }
    fun map(playlist: Playlist): PlaylistEntity {
        return if (playlist.id == null) PlaylistEntity(
            playlistName = playlist.playlistName,
            trackDescription = playlist.playlistDescription,
            coverPath = playlist.coverPath,
            trackList = gson.toJson(playlist.trackList),
            trackCount = playlist.trackCountInt,
            playlistYear = playlist.playlistYear
        ) else PlaylistEntity(playlist.id,
            playlist.playlistName,
            playlist.playlistDescription,
            playlist.coverPath,
            gson.toJson(playlist.trackList, trackListType),
            playlist.trackCountInt,
            playlist.playlistYear
        )
    }

    fun map(playlistEntity: PlaylistEntity): Playlist {
        return Playlist(
            playlistEntity.id,
            playlistEntity.playlistName,
            playlistEntity.trackDescription,
            playlistEntity.coverPath,
            gson.fromJson(playlistEntity.trackList, trackListType),
            playlistEntity.trackCount,
            playlistEntity.playlistYear
        )
    }

    fun map(tracksId: String): List<Int> = gson.fromJson(tracksId, trackListType)
}