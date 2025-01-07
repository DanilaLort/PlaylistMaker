package com.example.playlistmaker.ui.playlist.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.main.activity.MainActivity
import com.example.playlistmaker.ui.playlist.view_model.PlaylistViewModel
import com.example.playlistmaker.ui.tracks.Delay
import com.example.playlistmaker.ui.tracks.TrackAdapter
import com.example.playlistmaker.ui.tracks.TrackState
import com.example.playlistmaker.utils.debounce
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PlaylistFragment : Fragment() {
    private lateinit var binding: FragmentPlaylistBinding
    private lateinit var onTrackClickDebounce: (Track) -> Unit
    private val playlistViewModel: PlaylistViewModel by viewModel()
    private val trackAdapter = TrackAdapter { track ->
        onTrackClickDebounce(track)
    }
    private lateinit var playlist: Playlist
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        playlist = Gson().fromJson(requireArguments().getString(PLAYLIST_VALUE), Playlist::class.java)

        trackAdapter.longClickListener = TrackAdapter.TrackLongClickListener { track ->
            showTrackDialog(track)
        }

        playlistViewModel.getTracks(playlist.trackList)
        (activity as MainActivity).setBottomNavigationViewVisibility(false)

        playlistViewModel.getPlaylistTrackLiveData().observe(viewLifecycleOwner) { state ->
            if (state is TrackState.Content) {
                trackAdapter.tracks = state.tracks
                binding.playlists.adapter = trackAdapter

                var tracksTimeMillis = 0L
                state.tracks.map { tracksTimeMillis += it.trackTimeMillis!! }

                var trackTime = SimpleDateFormat("mm", Locale.getDefault()).format(tracksTimeMillis)

                binding.playlistDuration.text = formatPlaylistDuration(trackTime)
                Log.d("tracksTimeMillis", tracksTimeMillis.toString())
            }
        }

        playlistViewModel.getPlaylistLiveData().observe(viewLifecycleOwner) { updatedPlaylist ->
            playlist = updatedPlaylist
            playlistViewModel.getTracks(playlist.trackList)
            displayInfo()
        }

        displayInfo()

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.tracksBottomSheet).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }

        val addBottomSheetBehavior = BottomSheetBehavior.from(binding.additionalBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        addBottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }
                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = slideOffset + 1
            }
        })

        binding.returnButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.buttonMore.setOnClickListener {
            addBottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }

        binding.buttonShareBottomSheet.setOnClickListener {
            createMessage(trackAdapter.tracks)
        }

        binding.buttonShare.setOnClickListener {
            createMessage(trackAdapter.tracks)
        }

        binding.buttonEditInfo.setOnClickListener {
            val playlistBundle = bundleOf(PLAYLIST_VALUE to Gson().toJson(playlist))
            findNavController().navigate(R.id.action_playlistFragment2_to_playlistCreationFragment, playlistBundle)
        }

        binding.buttonDeletePlaylist.setOnClickListener {
            showPlaylistDialog()
        }

        Glide.with(view)
            .load(playlist.coverPath)
            .placeholder(R.drawable.ic_cover)
            .centerCrop()
            .transform(RoundedCorners(8))
            .into(binding.playlistCover)

        Glide.with(view)
            .load(playlist.coverPath)
            .placeholder(R.drawable.ic_cover)
            .centerCrop()
            .transform(RoundedCorners(8))
            .into(binding.playlistPanel.playlistCover)

        onTrackClickDebounce = debounce(Delay.ONE_SECOND_DELAY, viewLifecycleOwner.lifecycleScope, false) { track ->
            val trackBundle = bundleOf(TRACK_VALUE to Gson().toJson(track))
            findNavController().navigate(R.id.action_playlistFragment2_to_audioPlayerFragment, trackBundle)
        }

        binding.playlists.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun showTrackDialog(track: Track) {
        val confirmDialog = context?.let {
            MaterialAlertDialogBuilder(it)
                .setMessage(getString(R.string.wantDeleteTrack))
                .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                    dialog.cancel()
                }
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    playlistViewModel.deleteTrack(track, playlist)
                }
        }
        confirmDialog?.show()
    }

    private fun showPlaylistDialog() {
        val confirmDialog = context?.let {
            MaterialAlertDialogBuilder(it)
                .setMessage(getString(R.string.wantDeletePlaylist))
                .setNegativeButton(R.string.no) { dialog, _ ->
                    dialog.cancel()
                }
                .setPositiveButton(R.string.yes) { _, _ ->
                    playlistViewModel.deletePlaylist(playlist)
                    findNavController().navigateUp()
                }
        }
        confirmDialog?.show()
    }
    private fun displayInfo() {
        binding.playlistName.text = playlist.playlistName
        binding.playlistTrackCount.text = playlist.trackCount
        binding.playlistYear.text = playlist.playlistYear
        binding.playlistPanel.playlistName.text = playlist.playlistName
        binding.playlistPanel.trackCount.text = playlist.trackCount
    }

    private fun createMessage(trackList: List<Track>) {
        if (trackList.isNotEmpty()) {
            var message =
                "${playlist.playlistName}\n${playlist.playlistDescription}\n${playlist.trackCount}"
            trackList.forEachIndexed { index, track -> message += "\n$index ${track.artistName} ${track.trackName}" }
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share)))
        } else {
            Toast.makeText(context, getString(R.string.thereAreNoTracks), Toast.LENGTH_SHORT).show()
        }
    }


    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setBottomNavigationViewVisibility(false)
        displayInfo()
    }

    private fun formatPlaylistDuration(duration: String) : String {
        Log.d("tracksTimeMillis", (duration.toInt() in 2..4).toString())
        return duration + if (duration.toInt() == 1) getString(R.string.minute1)
                else if (duration.toInt() in 2..4) getString(R.string.minutes)
                else getString(R.string.minute)
    }

    override fun onDetach() {
        super.onDetach()
        (activity as MainActivity).setBottomNavigationViewVisibility(true)
    }

    companion object {
        private const val TRACK_VALUE = "TRACK_VALUE"
        private const val PLAYLIST_VALUE = "PLAYLIST_VALUE"
    }
}