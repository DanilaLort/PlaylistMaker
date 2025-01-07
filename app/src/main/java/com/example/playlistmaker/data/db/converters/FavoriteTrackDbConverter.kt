package com.example.playlistmaker.data.db.converters

import com.example.playlistmaker.data.db.entity.FavoriteTracksEntity
import com.example.playlistmaker.data.db.entity.PlaylistTrackEntity
import com.example.playlistmaker.domain.models.Track
import java.util.Calendar

class FavoriteTrackDbConverter {
    fun map(track: Track) : FavoriteTracksEntity {
        return FavoriteTracksEntity(
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.trackId,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            Calendar.getInstance().time.time
        )
    }

    fun map(track: FavoriteTracksEntity): Track {
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
}