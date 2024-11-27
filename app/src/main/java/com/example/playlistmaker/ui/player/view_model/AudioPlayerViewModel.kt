package com.example.playlistmaker.ui.player.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.domain.db.FavoriteTrackInteractor
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    private val mediaPlayerInteractor: MediaPlayerInteractor,
    private val favoriteTrackInteractor: FavoriteTrackInteractor
) : ViewModel() {
    private var playerStateLiveData = MutableLiveData<AudioPlayerState>()
    private var favoriteStateLiveData = MutableLiveData<Boolean>()
    private var timerJob: Job? = null
    fun getPlayerStateLiveData(): LiveData<AudioPlayerState> = playerStateLiveData
    fun getFavoriteStateLiveData(): LiveData<Boolean> = favoriteStateLiveData
    fun setUrl(url: String) {
        mediaPlayerInteractor.setUrl(url)
        prepareMediaPlayer()
    }
    fun favoriteButtonControl(track: Track) {
        if (track.isFavorite) {
            deleteFromFavorite(track)
        } else {
            addToFavorite(track)
        }
    }
    fun playbackControl() {
        when(mediaPlayerInteractor.getState()) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }
    fun pausePlayer() {
        stopTimeTask()
        mediaPlayerInteractor.pause {
            playerStateLiveData.postValue(AudioPlayerState.Pause)
        }
    }
    private fun startPlayer() {
        mediaPlayerInteractor.start {
            playerStateLiveData.postValue(AudioPlayerState.Start)
            startTimer()
        }
    }
    private fun destroyPlayer() {
        stopTimeTask()
        mediaPlayerInteractor.destroy()
    }
    private fun prepareMediaPlayer() {
        mediaPlayerInteractor.preparedListener {
            stopTimeTask()
            playerStateLiveData.postValue(AudioPlayerState.Prepared)
            mediaPlayerInteractor.setStatePrepared()
        }
        mediaPlayerInteractor.completionListener {
            stopTimeTask()
            mediaPlayerInteractor.setStatePrepared()
            playerStateLiveData.postValue(AudioPlayerState.Completion)

        }
    }
    private fun startTimer() {
        timerJob = viewModelScope.launch {
                while (mediaPlayerInteractor.getState() == STATE_PLAYING) {
                delay(300L)
                playerStateLiveData.postValue(AudioPlayerState.Playing(mediaPlayerInteractor.getCurrentPosition()))
            }
        }
    }
    private fun stopTimeTask() {
        if (mediaPlayerInteractor.getState() == STATE_PLAYING) timerJob?.cancel()
    }
    private fun addToFavorite(track: Track) {
        viewModelScope.launch {
            favoriteTrackInteractor.saveTrack(track)
            favoriteStateLiveData.postValue(true)
        }
    }
    private fun deleteFromFavorite(track: Track) {
        viewModelScope.launch {
            favoriteTrackInteractor.deleteTrack(track)
            favoriteStateLiveData.postValue(false)
        }
    }
     fun isTrackFavorite(id: Int) {
         viewModelScope.launch {
             favoriteTrackInteractor.getTrackId().collect {
                 favoriteStateLiveData.postValue(it.contains(id))
             }
         }
    }

    override fun onCleared() {
        super.onCleared()
        destroyPlayer()
    }
    companion object {
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
    }
}