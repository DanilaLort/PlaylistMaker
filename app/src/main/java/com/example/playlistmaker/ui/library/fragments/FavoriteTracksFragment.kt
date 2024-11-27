package com.example.playlistmaker.ui.library.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmetFavoriteTracksBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.library.view_model.FavoriteTracksState
import com.example.playlistmaker.ui.library.view_model.FavoriteTracksViewModel
import com.example.playlistmaker.ui.player.fragments.AudioPlayerFragment
import com.example.playlistmaker.ui.tracks.Delay
import com.example.playlistmaker.ui.tracks.TrackAdapter
import com.example.playlistmaker.utils.debounce
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : Fragment() {
    private lateinit var onTrackClickDebounce: (Track) -> Unit
    private var _binding: FragmetFavoriteTracksBinding? = null
    private val favoriteTracksViewModel: FavoriteTracksViewModel by viewModel()
    private val trackAdapter = TrackAdapter { track ->
        onTrackClickDebounce(track)
    }
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmetFavoriteTracksBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onTrackClickDebounce = debounce(Delay.ONE_SECOND_DELAY, viewLifecycleOwner.lifecycleScope, false) { track ->
            val trackBundle = bundleOf(AudioPlayerFragment.TRACK_VALUE to Gson().toJson(track))
            try {
                findNavController().navigate(
                    R.id.action_mediaLibraryFragment_to_audioPlayerFragment,
                    trackBundle
                )
            } catch (e: Exception) {
                Log.d("Favorite", e.message.toString())
            }
        }
        favoriteTracksViewModel.observeState().observe(viewLifecycleOwner) { state ->
            showMessage(HIDE_ALL)
            when (state) {
                is FavoriteTracksState.Loading -> {
                    showMessage(LOADING)
                }
                is FavoriteTracksState.Empty -> {
                    showMessage(EMPTY_LIBRARY)
                }
                is FavoriteTracksState.Content -> {
                    showMessage(CONTENT)
                    trackAdapter.tracks = state.tracks
                    binding.trackList.adapter = trackAdapter
                }
            }
        }
        favoriteTracksViewModel.fillData()
    }
    private fun showMessage(message: String) {
        when (message) {
            EMPTY_LIBRARY -> {
                binding.placeHolderCover.visibility = View.VISIBLE
                binding.message.visibility = View.VISIBLE
            }
            CONTENT -> {
                binding.trackList.visibility = View.VISIBLE
            }
            LOADING -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            HIDE_ALL -> {
                binding.progressBar.visibility = View.GONE
                binding.trackList.visibility = View.GONE
                binding.placeHolderCover.visibility = View.GONE
                binding.message.visibility = View.GONE
            }
        }
    }
    companion object {
        private const val EMPTY_LIBRARY = "EMPTY_LIBRARY"
        private const val CONTENT = "CONTENT"
        private const val LOADING = "LOADING"
        private const val HIDE_ALL = "HIDE_ALL"
        fun newInstance() = FavoriteTracksFragment()
    }
}