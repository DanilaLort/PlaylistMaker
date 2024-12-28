package com.example.playlistmaker.ui.playlist_creation.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistCreationBinding
import com.example.playlistmaker.ui.main.activity.MainActivity
import com.example.playlistmaker.ui.playlist_creation.view_model.PlaylistCreationViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistCreationFragment : Fragment() {
    private lateinit var binding: FragmentPlaylistCreationBinding
    private lateinit var callback: OnBackPressedCallback
    private val playlistCreationViewModel: PlaylistCreationViewModel by viewModel()
    private var playlistName: String? = null
    private var playlistDescription: String? = null
    private var coverPath: String? = null
    private var confirmDialog: MaterialAlertDialogBuilder? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistCreationBinding.inflate(layoutInflater)
        callback = (activity as MainActivity).onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            showDialog()
        }
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).setBottomNavigationViewVisibility(false)

        binding.returnButton.setOnClickListener {
            showDialog()
        }

        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                playlistCreationViewModel.saveTrackCover(uri)
            }
        }

        playlistCreationViewModel.getTrackCoverLiveData().observe(viewLifecycleOwner) { uri ->
            coverPath = uri.toString()
            Glide.with(this)
                .load(uri)
                .placeholder(R.drawable.ic_cover)
                .centerCrop()
                .transform(RoundedCorners(16))
                .into(binding.playlistCover)
        }

        binding.playlistCover.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.playlistName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.createButton.isEnabled = !s.isNullOrEmpty()
                binding.playlistName.isActivated = !s.isNullOrEmpty()
                playlistName = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        binding.playlistDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                playlistDescription = s.toString()
                binding.playlistDescription.isActivated = !s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        binding.createButton.setOnClickListener {
            if (playlistName != null)
                playlistCreationViewModel.savePlaylist(playlistName!!, playlistDescription, coverPath)
            findNavController().navigateUp()
        }
        confirmDialog = context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle(getString(R.string.finishCreating))
                .setMessage(getString(R.string.finshWarningMessage))
                .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                    dialog.cancel()
                }
                .setPositiveButton(getString(R.string.finish)) { _, _ ->
                    findNavController().navigateUp()
                }
        }


    }

    override fun onDetach() {
        super.onDetach()
        (activity as MainActivity).setBottomNavigationViewVisibility(true)
        callback.remove()
    }

    private fun showDialog() {
        Log.d("coverPath", "$coverPath")
        if (!playlistName.isNullOrEmpty() || !playlistDescription.isNullOrEmpty() || !coverPath.isNullOrEmpty())
            confirmDialog?.show()
        else
            findNavController().navigateUp()
    }
}