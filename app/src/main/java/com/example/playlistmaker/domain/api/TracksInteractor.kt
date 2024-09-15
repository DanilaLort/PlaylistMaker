package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.Resource
import com.example.playlistmaker.domain.models.Track

interface TracksInteractor {
    fun searchTrack(text: String, consumer: TracksConsumer)
    fun interface TracksConsumer {
        fun consume(foundTracks: Resource<List<Track>>)
    }
}