package com.example.playlistmaker.ui.player.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.ui.tracks.Delay

class AudioPlayerViewModel(private val mediaPlayerInteractor: MediaPlayerInteractor) : ViewModel() {
    private val handler = Handler(Looper.getMainLooper())
    private var playerStateLiveData = MutableLiveData<AudioPlayerState>()
    private lateinit var timerTask: Runnable
    fun getPlayerStateLiveData(): LiveData<AudioPlayerState> = playerStateLiveData

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
            timerTask = createUpdateTimerTask()
            handler.post(
                timerTask
            )
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
    private fun createUpdateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                playerStateLiveData.postValue(AudioPlayerState.Playing(mediaPlayerInteractor.getCurrentPosition()))
                handler.postDelayed(this, Delay.ONE_SECOND_DELAY / 3)
            }
        }
    }
    private fun stopTimeTask() {
        if (mediaPlayerInteractor.getState() == STATE_PLAYING) handler.removeCallbacks(timerTask)
    }
    companion object {
        fun getViewModelFactory(url: String): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                AudioPlayerViewModel(Creator.provideMediaPlayerInteractor(url))
            }
        }
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
    }
}