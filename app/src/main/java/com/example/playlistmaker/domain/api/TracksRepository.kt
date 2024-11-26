package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.Resource
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun searchTracks(text: String) : Flow<Resource<List<Track>>>
}