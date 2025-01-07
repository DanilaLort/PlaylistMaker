package com.example.playlistmaker.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Locale

@Entity(tableName = "playlist_track_table")
data class PlaylistTrackEntity(
    val trackName: String?,
    val artistName: String?,
    val trackTimeMillis: Long?,
    @PrimaryKey
    val id: Int?,
    val artworkUrl100: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?,
    val date: Long
)