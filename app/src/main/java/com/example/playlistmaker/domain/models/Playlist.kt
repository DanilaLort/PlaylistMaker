package com.example.playlistmaker.domain.models

data class Playlist(
    val id: Int?,
    val playlistName: String,
    val playlistDescription: String,
    val coverPath: String?,
    val trackList: List<Int>,
    var trackCountInt: Int
) {
    val trackCount: String get() {
        return when (trackCountInt) {
            1 -> "$trackCountInt трек"
            in 2..4 -> "$trackCountInt трека"
            else -> "$trackCountInt треков"
        }
    }
 }