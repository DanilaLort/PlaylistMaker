package com.example.playlistmaker.ui.player.activity

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.player.view_model.AudioPlayerViewModel
import com.google.gson.Gson

const val TRACK_INTENT_VALUE = "TRACK_INTENT_VALUE"

class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var buttonPlay: ImageButton
    private lateinit var trackTime: TextView
    private lateinit var binding: ActivityAudioPlayerBinding
    private lateinit var viewModel: AudioPlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val track = Gson().fromJson(intent.getStringExtra(TRACK_INTENT_VALUE), Track::class.java)
        val url = track.previewUrl ?: ""
        viewModel = ViewModelProvider(this, AudioPlayerViewModel.getViewModelFactory(url))[AudioPlayerViewModel::class.java]
        buttonPlay = binding.buttonPlay
        binding.returnButton.setOnClickListener {
            finish()
        }
        trackTime = binding.trackTime
        binding.trackCover.setImageResource(R.drawable.ic_cover)
        binding.trackTittle.text = track.trackName
        binding.artistName.text = track.artistName
        binding.trackDuration.text = track.trackTime
        binding.trackAlbum.text = track.collectionName
        binding.trackGenre.text = track.primaryGenreName
        binding.trackYear.text = track.releaseDate?.substring(0, 4)
        binding.trackCountry.text = track.country
        Glide.with(this)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.ic_cover)
            .transform(RoundedCorners(16))
            .into(binding.trackCover)
        prepareMediaPlayer()
        buttonPlay.setOnClickListener {
            viewModel.playbackControl()
        }
        viewModel.getTimerLiveData().observe(this) { time ->
            trackTime.text = time
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.destroyPlayer()
    }

    private fun prepareMediaPlayer() {
        viewModel.prepareMediaPlayer(
            {
                buttonPlay.isEnabled = true
                buttonPlay.setImageResource(R.drawable.ic_button_play)
                trackTime.setText(R.string.track_time)
            },
            {
                buttonPlay.setImageResource(R.drawable.ic_button_play)
                trackTime.setText(R.string.track_time)
            })
        viewModel.setOnPause {
            buttonPlay.setImageResource(R.drawable.ic_button_play)
        }
        viewModel.setOnStart {
            buttonPlay.setImageResource(R.drawable.ic_button_pause)
        }
    }
}
