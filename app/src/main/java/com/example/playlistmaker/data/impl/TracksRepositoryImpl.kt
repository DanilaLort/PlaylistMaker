package com.example.playlistmaker.data.impl

import android.util.Log
import com.example.playlistmaker.data.dto.TrackResponse
import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.network.NetworkClient
import com.example.playlistmaker.domain.Resource
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Locale

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(text: String): Flow<Resource<List<Track>>> = flow {
        try {
            val response = networkClient.doRequest(TrackSearchRequest(text))
            if (response.resultCode == 200) {
                emit(Resource.Success((response as TrackResponse).results.map {
                    Track(
                        it.trackName,
                        it.artistName,
                        SimpleDateFormat("mm:ss", Locale.getDefault()).format(it.trackTime),
                        it.trackId,
                        it.artworkUrl100,
                        it.collectionName,
                        it.releaseDate,
                        it.primaryGenreName,
                        it.country,
                        it.previewUrl
                    )
                }))
            } else {
                emit(Resource.Error("${response.resultCode}"))
            }
        } catch (e: Exception) {
            for (i in (networkClient.doRequest(TrackSearchRequest(text)) as TrackResponse).results) Log.d("searchRequest", i.toString())
            Log.d("searchRequest", "searchTracks - ${e.message}")
            emit(Resource.Error(e.message.toString()))
        }
    }

}