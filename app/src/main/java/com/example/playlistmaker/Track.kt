package com.example.playlistmaker

import android.util.Log
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Locale

data class Track (
    val trackName: String,
    val artistName: String,
    @SerializedName("trackTimeMillis")
    val trackTime: Long,
    val trackId: Int,
    val artworkUrl100: String,
) {
    fun getTrackTime(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTime)
    }

    override fun toString(): String {
        return "$trackName $trackId\n"
    }
}


