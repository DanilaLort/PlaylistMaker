package com.example.playlistmaker.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.Resource
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.ValueManagerInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.tracks.Delay
import com.example.playlistmaker.ui.tracks.SEARCH_HISTORY_SIZE
import com.example.playlistmaker.ui.tracks.TrackAdapter
import com.example.playlistmaker.ui.tracks.handler
import com.google.gson.Gson


class SearchActivity : AppCompatActivity() {
    private var searchText = SEARCH_TEXT_DEF
    private var reloadText = SEARCH_TEXT_DEF
    private lateinit var recyclerView: RecyclerView
    private val searchRunnable = Runnable { searchRequest(searchText) }
    private lateinit var trackManagerInteractor: ValueManagerInteractor<List<Track>>
    private val trackInteractor = Creator.provideTracksInteractor()
    private val trackAdapter = TrackAdapter { tracks, position ->
        if (clickDebounce()) {
            trackManagerInteractor.saveValue(object : ValueManagerInteractor.ValueConsumer<List<Track>> {
                override fun consume(): List<Track> {
                    val tracksHistory: ArrayList<Track> = ArrayList()
                    val savedHistory = trackManagerInteractor.getValue()
                    if (savedHistory.isNotEmpty()) tracksHistory.addAll(
                        savedHistory
                    )
                    if (tracksHistory.contains(tracks[position])) tracksHistory.remove(tracks[position])
                    tracksHistory.add(0, tracks[position])
                    if (tracksHistory.size > SEARCH_HISTORY_SIZE) tracksHistory.removeAt(
                        SEARCH_HISTORY_SIZE - 1
                    )
                    return tracksHistory
                }
            })
            val intent = Intent(
                this,
                AudioPlayerActivity::class.java
            )
            intent.putExtra(TRACK_INTENT_VALUE, Gson().toJson(tracks[position]))
            this.startActivity(
                intent
            )
        }
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        trackManagerInteractor = Creator.provideTrackManagerInteractor(this)
        setContentView(R.layout.activity_search)
        findViewById<Button>(R.id.search_to_main).setOnClickListener {
            finish()
        }
        val reloadButton = findViewById<Button>(R.id.reloadButton)
        recyclerView = findViewById(R.id.trackList)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val editText = findViewById<EditText>(R.id.search_text_field)
        val buttonClear = findViewById<Button>(R.id.button_clear)
        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && editText.text.isEmpty()) showSearchHistory(recyclerView)
            else showMessage(Message.VIEW_GONE)
        }

        findViewById<Button>(R.id.clearHistoryButton).setOnClickListener {
            if (recyclerView.adapter != null) clearRecyclerView(recyclerView.adapter as TrackAdapter)
            trackManagerInteractor.saveValue(object : ValueManagerInteractor.ValueConsumer<List<Track>> {
                override fun consume(): List<Track> {
                    return emptyList()
                }
            })
            showMessage(Message.VIEW_GONE)
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                buttonClear.isVisible = !s.isNullOrEmpty()
                if (s != null) {
                    if (s.isNotEmpty()) {
                        searchText = editText.text.toString()
                        searchDebounce()
                    }
                    else handler.removeCallbacks(searchRunnable)
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                showMessage(Message.VIEW_GONE)
                if (recyclerView.adapter != null) clearRecyclerView(recyclerView.adapter as TrackAdapter)
                if (searchText.isNotEmpty()) searchRequest(searchText)
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
            searchRequest(reloadText)
        }

    }

    private fun searchRequest(text: String) {
        showMessage(Message.VIEW_GONE)
        showMessage(Message.PROGRESS_BAR)
        trackInteractor.searchTrack(text, object : TracksInteractor.TracksConsumer {
            override fun consume(foundTracks: Resource<List<Track>>) {
                if (foundTracks is Resource.Success) {
                    val results = foundTracks.data as ArrayList<Track>
                    handler.post {
                        trackAdapter.tracks = results
                        if (results.isNotEmpty()) recyclerView.adapter = trackAdapter
                        else {
                            showMessage(Message.VIEW_GONE)
                            showMessage(Message.NOTHING_WAS_FOUND)
                        }
                    }
                } else {
                    handler.post {
                        showMessage(Message.VIEW_GONE)
                        showMessage(Message.COMMUNICATION_PROBLEMS)
                        reloadText = text
                    }
                }
            }
        })
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, Delay.TWO_SECOND_DELAY)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        findViewById<EditText>(R.id.search_text_field).setText(savedInstanceState.getString(
            SEARCH_TEXT, SEARCH_TEXT_DEF
        ))
    }

    private companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val SEARCH_TEXT_DEF = ""
    }

    private fun showSearchHistory(recyclerView: RecyclerView) {
        val trackHistory = trackManagerInteractor.getValue() as ArrayList<Track>
        if (trackHistory.isNotEmpty()) {
            showMessage(Message.SEARCH_HISTORY)
            trackAdapter.tracks = trackHistory
            recyclerView.adapter = trackAdapter
        }
    }

    private fun clearRecyclerView(adapter: TrackAdapter) {
        adapter.clearList()
    }

    fun showMessage(messageType: Message) {
        val placeholderMessage = findViewById<LinearLayout>(R.id.placeholder_message)
        val errorImage = findViewById<ImageView>(R.id.errorImage)
        val errorMessage = findViewById<TextView>(R.id.errorMessage)
        val reloadButton = findViewById<Button>(R.id.reloadButton)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        when (messageType) {
            Message.NOTHING_WAS_FOUND -> {
                reloadButton.visibility = View.GONE
                placeholderMessage.visibility = View.VISIBLE
                errorImage.setImageResource(R.drawable.nothing_was_found)
                errorMessage.setText(R.string.nothing_was_found)
            }
            Message.COMMUNICATION_PROBLEMS -> {
                placeholderMessage.visibility = View.VISIBLE
                errorImage.setImageResource(R.drawable.communication_problems)
                errorMessage.setText(R.string.communication_problems)
                reloadButton.visibility = View.VISIBLE
            }
            Message.SEARCH_HISTORY -> {
                findViewById<Button>(R.id.clearHistoryButton).visibility = View.VISIBLE
                findViewById<TextView>(R.id.search_history_title).visibility = View.VISIBLE
            }
            Message.VIEW_GONE -> {
                findViewById<Button>(R.id.clearHistoryButton).visibility = View.GONE
                findViewById<TextView>(R.id.search_history_title).visibility = View.GONE
                placeholderMessage.visibility = View.GONE
                progressBar.visibility = View.GONE
                if (recyclerView.adapter != null) clearRecyclerView(recyclerView.adapter as TrackAdapter)
            }
            Message.PROGRESS_BAR -> {
                progressBar.visibility = View.VISIBLE
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
}
