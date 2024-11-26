package com.example.playlistmaker.data.network

import android.util.Log
import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TrackSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(private val trackService: TrackApi) : NetworkClient {
    override suspend fun doRequest(dto: Any): Response {
        return withContext(Dispatchers.IO) {
            try {
                if (dto is TrackSearchRequest)  {
                    val resp = trackService.searchTracks(dto.text)
                    resp.apply { resultCode= 200 }
                } else {
                    Response().apply { resultCode = BadRequest }
                }
            } catch (e: Exception) {
                Log.d(
                    "RETROFIT_LOG",
                    "${e.message}"
                )
                Response().apply { resultCode = BadRequest }
            }
        }
    }

    companion object {
        const val BadRequest = 400
    }
}