package com.example.playlistmaker.ui.activity

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.tracks.Delay
import com.example.playlistmaker.ui.tracks.handler
import com.google.gson.Gson

const val TRACK_INTENT_VALUE = "TRACK_INTENT_VALUE"

class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var buttonPlay: ImageButton
    private lateinit var trackTime: TextView
    private lateinit var timerTask: Runnable
    private lateinit var mediaPlayerInteractor: MediaPlayerInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        val track = Gson().fromJson(intent.getStringExtra(TRACK_INTENT_VALUE), Track::class.java)
        val url = track.previewUrl

        buttonPlay = findViewById(R.id.buttonPlay)
        findViewById<ImageButton>(R.id.returnButton).setOnClickListener {
            finish()
        }
        buttonPlay.setOnClickListener {
            playbackControl()
        }
        trackTime = findViewById(R.id.trackTime)
        findViewById<ImageView>(R.id.trackCover).setImageResource(R.drawable.ic_cover)
        findViewById<TextView>(R.id.trackTittle).text = track.trackName
        findViewById<TextView>(R.id.artistName).text = track.artistName
        findViewById<TextView>(R.id.trackDuration).text = track.trackTime
        findViewById<TextView>(R.id.trackAlbum).text = track.collectionName
        findViewById<TextView>(R.id.trackGenre).text = track.primaryGenreName
        findViewById<TextView>(R.id.trackYear).text = track.releaseDate.substring(0, 4)
        findViewById<TextView>(R.id.trackCountry).text = track.country
        Glide.with(this)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.ic_cover)
            .transform(RoundedCorners(16))
            .into(findViewById(R.id.trackCover))
        mediaPlayerInteractor = Creator.provideMediaPlayerInteractor(url)
        prepareMediaPlayer()
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTimeTask()
        mediaPlayerInteractor.destroy()
    }

    private fun prepareMediaPlayer() {
        mediaPlayerInteractor.preparedListener {
            stopTimeTask()
            buttonPlay.setImageResource(R.drawable.ic_button_play)
            trackTime.setText(R.string.track_time)
        }
        mediaPlayerInteractor.completionListener {
            stopTimeTask()
            buttonPlay.setImageResource(R.drawable.ic_button_play)
            trackTime.setText(R.string.track_time)
            mediaPlayerInteractor.setStatePrepared()
        }
        mediaPlayerInteractor.setStatePrepared()
    }

    private fun startPlayer() {
        mediaPlayerInteractor.start {
            buttonPlay.setImageResource(R.drawable.ic_button_pause)
            timerTask = createUpdateTimerTask()
            handler.post(
                timerTask
            )
        }
    }

    private fun pausePlayer() {
        stopTimeTask()
        mediaPlayerInteractor.pause {
            buttonPlay.setImageResource(R.drawable.ic_button_play)
        }
    }

    private fun stopTimeTask() {
        if (mediaPlayerInteractor.getState() == STATE_PLAYING) handler.removeCallbacks(timerTask)
    }

    private fun playbackControl() {
        when(mediaPlayerInteractor.getState()) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun createUpdateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                trackTime.text = mediaPlayerInteractor.getCurrentPosition()
                handler.postDelayed(this, Delay.ONE_SECOND_DELAY / 3)
            }
        }
    }
    companion object {
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}
