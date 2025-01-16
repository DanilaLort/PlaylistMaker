package com.example.playlistmaker.ui.search.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.player.fragments.AudioPlayerFragment
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.ui.tracks.TrackState
import com.example.playlistmaker.ui.tracks.Delay
import com.example.playlistmaker.ui.tracks.TrackAdapter
import com.example.playlistmaker.utils.debounce
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentSearchBinding
    private lateinit var onTrackClickDebounce: (Track) -> Unit
    private val viewModel by viewModel<SearchViewModel>()
    private var searchText = SEARCH_TEXT_DEF
    private val trackAdapter = TrackAdapter { track ->
        onTrackClickDebounce(track)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("MissingInflatedId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onTrackClickDebounce = debounce(Delay.ONE_SECOND_DELAY, viewLifecycleOwner.lifecycleScope, false) { track ->
            viewModel.saveTracks(track)
            val trackBundle = bundleOf(TRACK_VALUE to Gson().toJson(track))
            findNavController().navigate(R.id.action_searchFragment2_to_audioPlayerFragment, trackBundle)
        }
        recyclerView = binding.trackList
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        if (savedInstanceState != null) {
            binding.searchTextField.setText(savedInstanceState.getString(SEARCH_TEXT))
        }
        val buttonClear = binding.buttonClear
        binding.searchTextField.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.searchTextField.text.isEmpty()) viewModel.showSearchHistory()
            else {
                if (binding.searchTextField.text.isEmpty()) showMessage(Message.VIEW_GONE)
            }
        }

        binding.clearHistoryButton.setOnClickListener {
            if (recyclerView.adapter != null) clearRecyclerView(recyclerView.adapter as TrackAdapter)
            viewModel.clearSearchHistory()
            showMessage(Message.VIEW_GONE)
        }
        viewModel.getLiveDataTrackState().observe(viewLifecycleOwner) { trackState ->
            when (trackState) {
                is TrackState.Loading -> {
                    Log.d("ShowMessage", "Loading")
                    showMessage(Message.VIEW_GONE)
                    showMessage(Message.PROGRESS_BAR)
                }
                is TrackState.Empty -> {
                    Log.d("ShowMessage", "Empty")
                    showMessage(Message.VIEW_GONE)
                    showMessage(Message.NOTHING_WAS_FOUND)
                }
                is TrackState.Error -> {
                    Log.d("ShowMessage", "Error")
                    showMessage(Message.VIEW_GONE)
                    showMessage(Message.COMMUNICATION_PROBLEMS)
                }
                is TrackState.Content -> {
                    Log.d("ShowMessage", "Content")
                    showMessage(Message.VIEW_GONE)
                    trackAdapter.tracks = trackState.tracks
                    recyclerView.adapter = trackAdapter
                }
                is TrackState.History -> {
                    Log.d("ShowMessage", "${trackState.tracks.isNotEmpty()}")
                    showMessage(Message.VIEW_GONE)
                    if (trackState.tracks.isNotEmpty()) {
                        showMessage(Message.SEARCH_HISTORY)
                        trackAdapter.tracks = trackState.tracks
                        recyclerView.adapter = trackAdapter
                    }
                }
            }
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                buttonClear.isVisible = !s.isNullOrEmpty()
                if (s != null) {
                    if (s.isNotEmpty()) {
                        searchText = binding.searchTextField.text.toString()
                        viewModel.searchDebounce(s.toString())
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        binding.searchTextField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.searchDebounce(searchText)
            }
            false
        }
        binding.searchTextField.addTextChangedListener(textWatcher)
        buttonClear.setOnClickListener {
            binding.searchTextField.setText(SEARCH_TEXT_DEF)
            if (recyclerView.adapter != null) clearRecyclerView(recyclerView.adapter as TrackAdapter)
            binding.searchTextField.clearFocus()
        }
        binding.reloadButton.setOnClickListener {
            viewModel.searchLatestText()
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, searchText)
    }

    private fun clearRecyclerView(adapter: TrackAdapter) {
        adapter.clearList()
    }

    private fun showMessage(messageType: Message) {
        Log.d("ShowMessage", "ShowMessage")
        when (messageType) {
            Message.NOTHING_WAS_FOUND -> {
                binding.reloadButton.visibility = View.GONE
                binding.placeholderMessage.visibility = View.VISIBLE
                binding.errorImage.setImageResource(R.drawable.ic_error_placeholder)
                binding.errorMessage.setText(R.string.nothingWasFound)
            }
            Message.COMMUNICATION_PROBLEMS -> {
                binding.placeholderMessage.visibility = View.VISIBLE
                binding.errorImage.setImageResource(R.drawable.ic_communication_problems)
                binding.errorMessage.setText(R.string.communication_problems)
                binding.reloadButton.visibility = View.VISIBLE
            }
            Message.SEARCH_HISTORY -> {
                binding.clearHistoryButton.visibility = View.VISIBLE
                binding.searchHistoryTitle.visibility = View.VISIBLE
            }
            Message.VIEW_GONE -> {
                binding.clearHistoryButton.visibility = View.GONE
                binding.searchHistoryTitle.visibility = View.GONE
                binding.placeholderMessage.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
                if (recyclerView.adapter != null) clearRecyclerView(recyclerView.adapter as TrackAdapter)
            }
            Message.PROGRESS_BAR -> {
                binding.progressBar.visibility = View.VISIBLE
            }
        }
    }

    enum class Message {
        NOTHING_WAS_FOUND,
        COMMUNICATION_PROBLEMS,
        SEARCH_HISTORY,
        VIEW_GONE,
        PROGRESS_BAR
    }
    private companion object {
        private const val TRACK_VALUE = "TRACK_VALUE"
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val SEARCH_TEXT_DEF = ""
    }
}
