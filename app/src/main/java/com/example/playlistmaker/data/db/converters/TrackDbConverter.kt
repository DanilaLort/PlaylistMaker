package com.example.playlistmaker.data.db.converters

import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.domain.models.Track
import java.util.Calendar

class TrackDbConverter {
    fun map(track: Track) : TrackEntity {
        return TrackEntity(
            track.trackName,
            track.artistName,
            track.trackTime,
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
    fun map(track: TrackEntity ): Track {
        return Track(
            track.trackName,
            track.artistName,
            track.trackTime,
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