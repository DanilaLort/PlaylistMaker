package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.Resource
import com.example.playlistmaker.domain.models.Track

interface TracksInteractor {
    fun searchMovies(text: String, consumer: TracksConsumer)
    interface TracksConsumer {
        fun consume(foundTracks: Resource<List<Track>>)
    }
}