package com.example.playlistmaker.ui.search.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.ui.player.fragments.AudioPlayerFragment
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.ui.search.view_model.TrackState
import com.example.playlistmaker.ui.tracks.Delay
import com.example.playlistmaker.ui.tracks.TrackAdapter
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentSearchBinding
    private val viewModel by viewModel<SearchViewModel>()
    private val handler = Handler(Looper.getMainLooper())
    private var searchText = SEARCH_TEXT_DEF
    private var reloadText = SEARCH_TEXT_DEF
    private val trackAdapter = TrackAdapter { track ->
        if (clickDebounce()) {
            viewModel.saveTracks(track)
            val trackBundle = bundleOf(AudioPlayerFragment.TRACK_VALUE to Gson().toJson(track))
            findNavController().navigate(R.id.action_searchFragment2_to_audioPlayerFragment, trackBundle)
        }
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
        val reloadButton = binding.reloadButton
        recyclerView = binding.trackList
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val editText = binding.searchTextField
        if (savedInstanceState != null) {
            editText.setText(savedInstanceState.getString(SEARCH_TEXT))
        }
        val buttonClear = binding.buttonClear
        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && editText.text.isEmpty()) viewModel.showSearchHistory()
            else showMessage(Message.VIEW_GONE)
        }

        binding.clearHistoryButton.setOnClickListener {
            if (recyclerView.adapter != null) clearRecyclerView(recyclerView.adapter as TrackAdapter)
            viewModel.clearSearchHistory()
            showMessage(Message.VIEW_GONE)
        }
        viewModel.getLiveDataTrackState().observe(viewLifecycleOwner) { trackState ->
            when (trackState) {
                is TrackState.Loading -> {
                    showMessage(Message.VIEW_GONE)
                    showMessage(Message.PROGRESS_BAR)
                }
                is TrackState.Empty -> {
                    showMessage(Message.VIEW_GONE)
                    showMessage(Message.NOTHING_WAS_FOUND)
                }
                is TrackState.Error -> {
                    showMessage(Message.VIEW_GONE)
                    showMessage(Message.COMMUNICATION_PROBLEMS)
                    reloadText = searchText
                }
                is TrackState.Content -> {
                    showMessage(Message.VIEW_GONE)
                    trackAdapter.tracks = trackState.tracks
                    recyclerView.adapter = trackAdapter
                }
                is TrackState.History -> {
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
                        searchText = editText.text.toString()
                        viewModel.searchDebounce(s.toString())
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.searchDebounce(searchText)
            }
            false
        }
        editText.addTextChangedListener(textWatcher)
        buttonClear.setOnClickListener {
            editText.setText(SEARCH_TEXT_DEF)
            if (recyclerView.adapter != null) clearRecyclerView(recyclerView.adapter as TrackAdapter)
            editText.clearFocus()
        }
        reloadButton.setOnClickListener {
            viewModel.searchDebounce(reloadText)
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
        when (messageType) {
            Message.NOTHING_WAS_FOUND -> {
                binding.reloadButton.visibility = View.GONE
                binding.placeholderMessage.visibility = View.VISIBLE
                binding.errorImage.setImageResource(R.drawable.error_placeholder)
                binding.errorMessage.setText(R.string.nothingWasFound)
            }
            Message.COMMUNICATION_PROBLEMS -> {
                binding.placeholderMessage.visibility = View.VISIBLE
                binding.errorImage.setImageResource(R.drawable.communication_problems)
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

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, Delay.ONE_SECOND_DELAY)
        }
        return current
    }

    private var isClickAllowed = true
    enum class Message {
        NOTHING_WAS_FOUND,
        COMMUNICATION_PROBLEMS,
        SEARCH_HISTORY,
        VIEW_GONE,
        PROGRESS_BAR
    }
    private companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val SEARCH_TEXT_DEF = ""
    }
}
