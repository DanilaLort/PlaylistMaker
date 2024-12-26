package com.example.playlistmaker.domain.impl

import android.net.Uri
import com.example.playlistmaker.domain.api.TrackCoverInteractor
import com.example.playlistmaker.domain.api.TrackCoverRepository

class TrackCoverInteractorImpl(
    private val trackCoverRepository: TrackCoverRepository
) : TrackCoverInteractor {
    override fun saveTrackCover(uri: Uri): Uri {
        return trackCoverRepository.saveTrackCover(uri)
    }
}