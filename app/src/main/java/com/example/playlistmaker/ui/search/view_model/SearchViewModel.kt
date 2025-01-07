package com.example.playlistmaker.ui.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.ValueManagerInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.tracks.Delay
import com.example.playlistmaker.ui.tracks.TrackState
import com.example.playlistmaker.utils.debounce
import kotlinx.coroutines.launch

class SearchViewModel(
    private val trackManagerInteractor: ValueManagerInteractor<List<Track>>,
    private val trackInteractor: TracksInteractor
) : ViewModel() {
    private val trackState = MutableLiveData<TrackState>()
    private var latestSearchText: String? = null
    private val trackSearchDebounce = debounce<String>(Delay.TWO_SECOND_DELAY, viewModelScope, true) { text ->
        searchRequest(text) }
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
        if (latestSearchText != changedText) {
            latestSearchText = changedText
            trackSearchDebounce(changedText)
        }
    }

    fun searchLatestText() {
        latestSearchText?.let { trackSearchDebounce(it) }
    }
    private fun searchRequest(text: String) {
        trackState.postValue(TrackState.Loading)
        viewModelScope.launch {
            trackInteractor
                .searchTrack(text)
                .collect { pair ->
                    if (pair.first != null) {
                        if (pair.first!!.isEmpty()) trackState.postValue(TrackState.Empty)
                        else trackState.postValue(TrackState.Content(pair.first!!))
                    } else {
                        trackState.postValue(pair.second?.let { TrackState.Error(it) })
                    }
                }
        }
    }
    companion object {
        private const val SEARCH_HISTORY_SIZE = 10
    }
}