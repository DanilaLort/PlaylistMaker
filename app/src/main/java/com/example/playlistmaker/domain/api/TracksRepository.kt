package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.Resource
import com.example.playlistmaker.domain.models.Track

interface TracksRepository {
    fun searchTracks(text: String) : Resource<List<Track>>
}