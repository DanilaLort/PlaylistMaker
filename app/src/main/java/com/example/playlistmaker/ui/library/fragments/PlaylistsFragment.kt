package com.example.playlistmaker.ui.library.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.ui.library.view_model.PlaylistsFragmentViewModel
import com.example.playlistmaker.ui.playlists.PlaylistSquareAdapter
import com.example.playlistmaker.ui.playlists.PlaylistState
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!
    private val playlistViewModel: PlaylistsFragmentViewModel by viewModel()
    private lateinit var playlistClickListener: (Playlist) -> Unit
    private val adapter = PlaylistSquareAdapter {
        playlistClickListener(it)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.playlists.layoutManager = GridLayoutManager(this.context, TWO_COLUMNS)
        playlistViewModel.getLiveDataPlaylistState().observe(viewLifecycleOwner) { playlistState ->
            showMessage(HIDE_ALL)
            when (playlistState) {
                is PlaylistState.Loading -> {
                    showMessage(LOADING)
                }
                is PlaylistState.Empty -> {
                    showMessage(EMPTY_LIBRARY)
                }
                is PlaylistState.Content -> {
                    showMessage(CONTENT)
                    adapter.playlists = playlistState.playlists
                    binding.playlists.adapter = adapter
                }
                else -> {}
            }
        }

        playlistViewModel.getPlaylists()
        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_mediaLibraryFragment_to_playlistCreationFragment, bundleOf())
        }

        playlistClickListener = { playlist ->
            val playlistBundle = bundleOf(PLAYLIST_VALUE to Gson().toJson(playlist))
            findNavController().navigate(R.id.action_mediaLibraryFragment_to_playlistFragment2, playlistBundle)
        }
    }

    override fun onResume() {
        super.onResume()
        playlistViewModel.getPlaylists()
    }

    private fun showMessage(message: String) {
        when (message) {
            EMPTY_LIBRARY -> {
                binding.placeHolderCover.visibility = View.VISIBLE
                binding.message.visibility = View.VISIBLE
            }
            CONTENT -> {
                binding.playlists.visibility = View.VISIBLE
            }
            LOADING -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            HIDE_ALL -> {
                binding.progressBar.visibility = View.GONE
                binding.playlists.visibility = View.GONE
                binding.placeHolderCover.visibility = View.GONE
                binding.message.visibility = View.GONE
            }
        }
    }

    companion object {
        private const val TWO_COLUMNS = 2
        private const val PLAYLIST_VALUE = "PLAYLIST_VALUE"
        private const val EMPTY_LIBRARY = "EMPTY_LIBRARY"
        private const val CONTENT = "CONTENT"
        private const val LOADING = "LOADING"
        private const val HIDE_ALL = "HIDE_ALL"
        fun newInstance() = PlaylistsFragment()
    }
}