package com.example.playlistmaker.ui.search.view_model

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.App
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.Resource
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.ValueManagerRepository
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.tracks.Delay

class SearchActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val trackManagerRepository: ValueManagerRepository<List<Track>> = (application as App).getTrackRepository()
    private val trackInteractor: TracksInteractor = Creator.provideTracksInteractor()
    private val handler = Handler(Looper.getMainLooper())
    private val trackState = MutableLiveData<TrackState>()
    private var latestSearchText: String? = null
    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }
    fun saveTracks(track: Track) {
        val tracksHistory: ArrayList<Track> = ArrayList()
        val savedHistory = trackManagerRepository.getValue()
        if (savedHistory.isNotEmpty()) tracksHistory.addAll(
            savedHistory
        )
        if (tracksHistory.contains(track)) tracksHistory.remove(track)
        tracksHistory.add(0, track)
        if (tracksHistory.size > SEARCH_HISTORY_SIZE) tracksHistory.removeAt(
            SEARCH_HISTORY_SIZE - 1
        )
        trackManagerRepository.saveValue(tracksHistory)
    }
    fun getLiveDataTrackState(): LiveData<TrackState> = trackState
    fun showSearchHistory() {
        val trackHistory = trackManagerRepository.getValue()
        if (trackHistory.isNotEmpty()) {
            trackState.value = TrackState.History(trackHistory)
        }
    }
    fun clearSearchHistory() {
        trackManagerRepository.saveValue(emptyList())
    }
    fun searchDebounce(changedText: String) {
        try {
            if (latestSearchText == changedText) {
                return
            }
            this.latestSearchText = changedText
            handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

            val searchRunnable = Runnable { searchRequest(changedText) }

            val postTime = SystemClock.uptimeMillis() + Delay.TWO_SECOND_DELAY
            handler.postAtTime(
                searchRunnable,
                SEARCH_REQUEST_TOKEN,
                postTime,
            )
        } catch (e: Exception) {
            Log.d("searchRequest", "searchDebounce - ${e.message}")
        }
    }
    private fun searchRequest(text: String) {
        try {
            trackState.postValue(TrackState.Loading)
            trackInteractor.searchTrack(
                text
            ) { foundTracks: Resource<List<Track>> ->
                if (foundTracks is Resource.Success) {
                    if (foundTracks.data.isEmpty()) trackState.postValue(TrackState.Empty)
                    else {
                        trackState.postValue(TrackState.Content(foundTracks.data))
                    }
                } else if (foundTracks is Resource.Error) {
                    trackState.postValue(TrackState.Error(foundTracks.message))
                }
            }
        } catch (e: Exception) {
            Log.d("searchRequest", "searchRequest - ${e.message}")
        }
    }
    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as App)
                SearchActivityViewModel(application)
            }
        }
        private val SEARCH_REQUEST_TOKEN = Any()
        private const val SEARCH_HISTORY_SIZE = 10
    }
}