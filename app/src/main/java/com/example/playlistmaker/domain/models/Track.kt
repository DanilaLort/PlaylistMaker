package com.example.playlistmaker.domain.models

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Locale

data class Track (
    val trackName: String,
    val artistName: String,
    @SerializedName("trackTimeMillis")
    val trackTime: String,
    val trackId: Int,
    val artworkUrl100: String?,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
) {

    fun getCoverArtwork() = artworkUrl100?.replaceAfterLast('/',"512x512bb.jpg")

    override fun toString(): String {
        return "$trackName $trackId\n"
    }
}


