package com.example.playlistmaker.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val playlistName: String,
    val trackDescription: String,
    val coverPath: String?,
    val trackList: String,
    val trackCount: Int,
    val date: Long
    )