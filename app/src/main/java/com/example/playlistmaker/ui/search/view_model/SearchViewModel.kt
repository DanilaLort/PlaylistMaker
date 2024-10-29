package com.example.playlistmaker.ui.search.view_model

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.Resource
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.ValueManagerInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.tracks.Delay

class SearchViewModel(
    private val trackManagerInteractor: ValueManagerInteractor<List<Track>>,
    private val trackInteractor: TracksInteractor
) : ViewModel() {
    private val handler = Handler(Looper.getMainLooper())
    private val trackState = MutableLiveData<TrackState>()
    private var latestSearchText: String? = null
    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }
    fun saveTracks(track: Track) {
        trackManagerInteractor.saveValue {
            val tracksHistory: ArrayList<Track> = ArrayList()
            val savedHistory = trackManagerInteractor.getValue()
            if (savedHistory.isNotEmpty()) tracksHistory.addAll(
                savedHistory
            )
            if (tracksHistory.contains(track)) tracksHistory.remove(track)
            tracksHistory.add(0, track)
            if (tracksHistory.size > SEARCH_HISTORY_SIZE) tracksHistory.removeAt(
                SEARCH_HISTORY_SIZE - 1
            )
            tracksHistory
        }
    }
    fun getLiveDataTrackState(): LiveData<TrackState> = trackState
    fun showSearchHistory() {
        val trackHistory = trackManagerInteractor.getValue()
        if (trackHistory.isNotEmpty()) {
            trackState.value = TrackState.History(trackHistory)
        }
    }
    fun clearSearchHistory() {
        trackManagerInteractor.saveValue { emptyList() }
    }
    fun searchDebounce(changedText: String) {
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
    }
    private fun searchRequest(text: String) {
        trackState.postValue(TrackState.Loading)
        trackInteractor.searchTrack(text) { foundTracks: Resource<List<Track>> ->
                if (foundTracks is Resource.Success) {
                    if (foundTracks.data.isEmpty()) trackState.postValue(TrackState.Empty)
                    else {
                        trackState.postValue(TrackState.Content(foundTracks.data))
                    }
                } else if (foundTracks is Resource.Error) {
                    trackState.postValue(TrackState.Error(foundTracks.message))
                }
            }
    }
    companion object {
        private val SEARCH_REQUEST_TOKEN = Any()
        private const val SEARCH_HISTORY_SIZE = 10
    }
}