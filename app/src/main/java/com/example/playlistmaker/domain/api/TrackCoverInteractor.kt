package com.example.playlistmaker.domain.api

import android.net.Uri

interface TrackCoverInteractor {
    fun saveTrackCover(uri: Uri): Uri

}