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
    private var timerLiveData = MutableLiveData(TIMER_DEF)
    private lateinit var timerTask: Runnable
    private lateinit var onPause: OnPause
    private lateinit var onStart: OnStart

    fun getTimerLiveData(): LiveData<String> = timerLiveData
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
            onPause.onPause()
        }
    }
    private fun startPlayer() {
        mediaPlayerInteractor.start {
            onStart.onStart()
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
    fun setOnStart(onStart: OnStart) {
        this.onStart = onStart
    }
    fun setOnPause(onPause: OnPause) {
        this.onPause = onPause
    }
    fun prepareMediaPlayer(onPrepare: OnPrepare, onCompletion: OnCompletion) {
        mediaPlayerInteractor.preparedListener {
            stopTimeTask()
            onPrepare.onPrepare()
        }
        mediaPlayerInteractor.completionListener {
            stopTimeTask()
            onCompletion.onCompletion()
            mediaPlayerInteractor.setStatePrepared()
        }
        mediaPlayerInteractor.setStatePrepared()
    }
    private fun createUpdateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                timerLiveData.postValue(mediaPlayerInteractor.getCurrentPosition())
                handler.postDelayed(this, Delay.ONE_SECOND_DELAY / 3)
            }
        }
    }
    private fun stopTimeTask() {
        if (mediaPlayerInteractor.getState() == STATE_PLAYING) handler.removeCallbacks(timerTask)
    }
    fun interface OnPause {
        fun onPause()
    }
    fun interface OnStart {
        fun onStart()
    }
    fun interface OnPrepare {
        fun onPrepare()
    }
    fun interface OnCompletion {
        fun onCompletion()
    }
    companion object {
        fun getViewModelFactory(url: String): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                AudioPlayerViewModel(Creator.provideMediaPlayerInteractor(url))
            }
        }
        private const val TIMER_DEF = "00:00"
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
    }
}