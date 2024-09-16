package com.example.playlistmaker.ui.search.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.ui.player.activity.AudioPlayerActivity
import com.example.playlistmaker.ui.search.view_model.SearchActivityViewModel
import com.example.playlistmaker.ui.search.view_model.TrackState
import com.example.playlistmaker.ui.tracks.Delay
import com.example.playlistmaker.ui.tracks.TrackAdapter
import com.google.gson.Gson

class SearchActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: SearchActivityViewModel
    private val handler = Handler(Looper.getMainLooper())
    private var searchText = SEARCH_TEXT_DEF
    private var reloadText = SEARCH_TEXT_DEF
    private val trackAdapter = TrackAdapter { track ->
        if (clickDebounce()) {
            viewModel.saveTracks(track)
            val intent = Intent(
                this,
                AudioPlayerActivity::class.java
            )
            intent.putExtra(TRACK_INTENT_VALUE, Gson().toJson(track))
            this.startActivity(
                intent
            )
        }
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, SearchActivityViewModel.getViewModelFactory())[SearchActivityViewModel::class.java]
        binding.searchToMain.setOnClickListener {
            finish()
        }
        val reloadButton = binding.reloadButton
        recyclerView = binding.trackList
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val editText = binding.searchTextField
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
        viewModel.getLiveDataTrackState().observe(this) { trackState ->
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
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
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

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        binding.searchTextField.setText(savedInstanceState.getString(
            SEARCH_TEXT, SEARCH_TEXT_DEF
        ))
    }
    private fun clearRecyclerView(adapter: TrackAdapter) {
        adapter.clearList()
    }

    private fun showMessage(messageType: Message) {
        when (messageType) {
            Message.NOTHING_WAS_FOUND -> {
                binding.reloadButton.visibility = View.GONE
                binding.placeholderMessage.visibility = View.VISIBLE
                binding.errorImage.setImageResource(R.drawable.nothing_was_found)
                binding.errorMessage.setText(R.string.nothing_was_found)
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
        const val TRACK_INTENT_VALUE = "TRACK_INTENT_VALUE"
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val SEARCH_TEXT_DEF = ""
    }
}
