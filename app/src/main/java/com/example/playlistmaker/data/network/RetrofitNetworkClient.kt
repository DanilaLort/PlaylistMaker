package com.example.playlistmaker.data.network

import android.util.Log
import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TrackSearchRequest

class RetrofitNetworkClient(private val trackService: TrackApi) : NetworkClient {

//    private val itunesBaseURL = "https://itunes.apple.com"
//
//    private val retrofit = Retrofit.Builder()
//        .baseUrl(itunesBaseURL)
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//
//    private val trackService = retrofit.create(TrackApi::class.java)

    override fun doRequest(dto: Any): Response {
        return try {
            if (dto is TrackSearchRequest) {
                val resp = trackService.searchTracks(dto.text).execute()

                val body = resp.body() ?: Response()

                body.apply { resultCode = resp.code() }
            } else {
                Response().apply { resultCode =BadRequest }
            }
        } catch (e: Exception) {
            Log.d(
                "RETROFIT_LOG",
                "${e.message}"
            )
            Response().apply { resultCode = BadRequest }
        }
    }

    companion object {
        const val BadRequest = 400
    }
}