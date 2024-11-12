package com.example.playlistmaker.ui.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api.MediaPlayerInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    private val mediaPlayerInteractor: MediaPlayerInteractor
) : ViewModel() {
    private var playerStateLiveData = MutableLiveData<AudioPlayerState>()
    private var timerJob: Job? = null
    fun getPlayerStateLiveData(): LiveData<AudioPlayerState> = playerStateLiveData
    fun setUrl(url: String) {
        mediaPlayerInteractor.setUrl(url)
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
    fun destroyPlayer() {
        stopTimeTask()
        mediaPlayerInteractor.destroy()
    }
    fun prepareMediaPlayer() {
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
    companion object {
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
    }
}