package com.example.playlistmaker.ui.player.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.main.activity.MainActivity
import com.example.playlistmaker.ui.player.view_model.AudioPlayerState
import com.example.playlistmaker.ui.player.view_model.AudioPlayerViewModel
import com.example.playlistmaker.ui.playlists.PlaylistRectangularAdapter
import com.example.playlistmaker.ui.playlists.PlaylistState
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class AudioPlayerFragment : Fragment() {
    private lateinit var buttonPlay: ImageButton
    private lateinit var trackTime: TextView
    private lateinit var binding: FragmentAudioPlayerBinding
    private lateinit var playlistClickListener: (Playlist) -> Unit
    private val adapter = PlaylistRectangularAdapter {
        playlistClickListener(it)
    }
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
            viewModel.setUrl(url)
        buttonPlay.setOnClickListener {
            viewModel.playbackControl()
        }
        viewModel.isTrackFavorite(track.trackId!!)
        viewModel.getFavoriteStateLiveData().observe(viewLifecycleOwner) {
            track.isFavorite = it
            if (it) {
                binding.buttonAddToFavorites.setImageResource(R.drawable.ic_add_to_favorites_active)
            } else {
                binding.buttonAddToFavorites.setImageResource(R.drawable.ic_add_to_favorites)
            }
        }
        binding.buttonAddToFavorites.setOnClickListener {
            viewModel.favoriteButtonControl(track)
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

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistsBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.buttonAddToPlayList.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }

        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_audioPlayerFragment_to_playlistCreationFragment)
        }

        playlistClickListener = { playlist ->
            viewModel.saveTrack(track, playlist)
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }
                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                        viewModel.getPlaylists()
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = slideOffset + 1
            }
        })

        binding.playlists.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        viewModel.getLiveDataPlaylistState().observe(viewLifecycleOwner) { state ->
            when(state) {
                is PlaylistState.Content -> {
                    adapter.playlists = state.playlists
                    binding.playlists.adapter = adapter
                }

                is PlaylistState.TrackAlreadyAdded -> {
                    Toast.makeText(context,
                        getString(R.string.AlreadyAdded)+ " ${state.playlistName}", Toast.LENGTH_SHORT).show()
                }

                is PlaylistState.TrackAdded -> {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    Toast.makeText(context,
                        getString(R.string.AddedToPlaylist) + " ${state.playlistName}", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setBottomNavigationViewVisibility(false)
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDetach() {
        super.onDetach()
        (activity as MainActivity).setBottomNavigationViewVisibility(true)
    }

    companion object {
        const val TRACK_VALUE = "TRACK_VALUE"
    }
}
