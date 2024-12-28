package com.example.playlistmaker.domain.models

data class Playlist(
    val id: Int?,
    val playlistName: String,
    val playlistDescription: String,
    val coverPath: String?,
    val trackList: List<Int>,
    var trackCount: Int
) {
    fun getTrackCount(): String {
        return if (trackCount != 1) "$trackCount треков"
        else "$trackCount трек"
    }
 }