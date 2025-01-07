package com.example.playlistmaker.domain.models

import java.time.Year

data class Playlist(
    val id: Int?,
    val playlistName: String,
    val playlistDescription: String,
    val coverPath: String?,
    val trackList: List<Int>,
    var trackCountInt: Int,
    val playlistYear: String
) {
    val trackCount: String get() {
        return if (trackCountInt != 1) "$trackCountInt треков"
        else "$trackCountInt трек"
    }
 }