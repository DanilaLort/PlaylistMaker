package com.example.playlistmaker.domain.api

import android.net.Uri

interface TrackCoverRepository {
    fun saveTrackCover(uri: Uri): Uri
}