package com.example.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

const val TRACK_INTENT_VALUE = "TRACK_INTENT_VALUE"

class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var url: String
    private lateinit var buttonPlay: ImageButton
    private lateinit var trackTime: TextView
    private var playerState = STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()
    private lateinit var timerTask: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        val track = Gson().fromJson(intent.getStringExtra(TRACK_INTENT_VALUE), Track::class.java)
        url = track.previewUrl

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
        findViewById<TextView>(R.id.trackDuration).text = track.getTrackTime()
        findViewById<TextView>(R.id.trackAlbum).text = track.collectionName
        findViewById<TextView>(R.id.trackGenre).text = track.primaryGenreName
        findViewById<TextView>(R.id.trackYear).text = track.releaseDate.substring(0, 4)
        findViewById<TextView>(R.id.trackCountry).text = track.country
        Glide.with(this)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.ic_cover)
            .transform(RoundedCorners(16))
            .into(findViewById(R.id.trackCover))
        preparePlayer()
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        stopTimeTask()
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            buttonPlay.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            buttonPlay.setImageResource(R.drawable.ic_button_play)
            stopTimeTask()
            trackTime.setText(R.string.track_time)
            playerState = STATE_PREPARED
        }
    }

        private fun startPlayer() {
        mediaPlayer.start()
        buttonPlay.setImageResource(R.drawable.ic_button_pause)
            playerState = STATE_PLAYING
            timerTask = createUpdateTimerTask()
            handler.post(
                timerTask
            )
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        stopTimeTask()
        buttonPlay.setImageResource(R.drawable.ic_button_play)
        playerState = STATE_PAUSED
//        Log.d(
//            "TRACK_LOG",
//            "$timerTask"
//        )
    }

    private fun stopTimeTask() {
        if (playerState == STATE_PLAYING) handler.removeCallbacks(timerTask)
    }

    private fun playbackControl() {
        when(playerState) {
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
                trackTime.text = SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()).format(mediaPlayer.currentPosition
                    )
                handler.postDelayed(this, Delay.ONE_SECOND_DELAY / 3)
            }
        }
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}