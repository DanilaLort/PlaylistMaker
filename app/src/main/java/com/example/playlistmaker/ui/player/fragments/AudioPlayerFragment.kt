package com.example.playlistmaker.ui.player.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.main.activity.MainActivity
import com.example.playlistmaker.ui.player.view_model.AudioPlayerState
import com.example.playlistmaker.ui.player.view_model.AudioPlayerViewModel
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class AudioPlayerFragment : Fragment() {
    private lateinit var buttonPlay: ImageButton
    private lateinit var trackTime: TextView
    private lateinit var binding: FragmentAudioPlayerBinding
    private val viewModel by viewModel<AudioPlayerViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAudioPlayerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val track = Gson().fromJson(requireArguments().getString(TRACK_VALUE), Track::class.java)
        val url = track.previewUrl ?: ""
        buttonPlay = binding.buttonPlay
        viewModel.setUrl(url)
        binding.returnButton.setOnClickListener {
            findNavController().navigateUp()
        }
        (activity as MainActivity).setBottomNavigationViewVisibility(false)
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
        viewModel.prepareMediaPlayer()
        buttonPlay.setOnClickListener {
            viewModel.playbackControl()
        }
        viewModel.getPlayerStateLiveData().observe(viewLifecycleOwner) { state ->
            when (state) {
                is AudioPlayerState.Prepared -> {
                    buttonPlay.isEnabled = true
                    buttonPlay.setImageResource(R.drawable.ic_button_play)
                    trackTime.setText(R.string.trackTime)
                }
                is AudioPlayerState.Completion -> {
                    buttonPlay.setImageResource(R.drawable.ic_button_play)
                    trackTime.setText(R.string.trackTime)
                }
                is AudioPlayerState.Start -> {
                    buttonPlay.setImageResource(R.drawable.ic_button_pause)
                }
                is AudioPlayerState.Pause -> {
                    buttonPlay.setImageResource(R.drawable.ic_button_play)
                }
                is AudioPlayerState.Playing -> {
                    trackTime.text = state.timer
                }
            }
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

    override fun onDetach() {
        super.onDetach()
        (activity as MainActivity).setBottomNavigationViewVisibility(true)
    }

    companion object {
        const val TRACK_VALUE = "TRACK_VALUE"
    }
}
