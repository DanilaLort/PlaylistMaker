package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.text.SimpleDateFormat
import java.util.Locale

class SearchActivity : AppCompatActivity() {
    private var searchText = SEARCH_TEXT_DEF
    private var reloadText = ""
    val trackList: ArrayList<Track> = ArrayList()
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        findViewById<Button>(R.id.search_to_main).setOnClickListener {
            finish()
        }
        val retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.baseUrl))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val getTrack = retrofit.create(TrackApi::class.java)
        val reloadButton = findViewById<Button>(R.id.reloadButton)
        val recyclerView = findViewById<RecyclerView>(R.id.trackList)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val editText = findViewById<EditText>(R.id.search_text_field)
        val buttonClear = findViewById<Button>(R.id.button_clear)
        buttonClear.visibility = View.GONE
        val enqueueSample = object : Callback<TrackResponse> {
            override fun onResponse(
                call: Call<TrackResponse>,
                response: Response<TrackResponse>
            ) {
                if (response.body()?.results?.isEmpty() == true) {
                    if (recyclerView.adapter != null) clearRecyclerView(recyclerView.adapter as TrackAdapter)
                    showMessage(R.string.nothing_was_found)
                } else {
                    showMessage(View.GONE)
                    if (response.body() != null) {
                        val trackAdapter = TrackAdapter(response.body()!!.results)
                        recyclerView.adapter = trackAdapter
                        Log.d(
                            "TRANSLATION_LOG",
                            "${response.body()?.results?.get(0)?.trackTime}"
                        )
                    }
                }
            }

            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                if (recyclerView.adapter != null) clearRecyclerView(recyclerView.adapter as TrackAdapter)
                showMessage(R.string.communication_problems)
            }
        }
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                buttonClear.isVisible = !s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {
                searchText = editText.text.toString()
            }
        }
        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (!searchText.isNullOrEmpty()) getTrack.search(searchText)
                    .enqueue(enqueueSample)
                true
            }
            false
        }
        editText.addTextChangedListener(textWatcher)
        buttonClear.setOnClickListener {
            editText.setText("")
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            if (recyclerView.adapter != null) clearRecyclerView(recyclerView.adapter as TrackAdapter)
        }
        reloadButton.setOnClickListener {
            getTrack.search(reloadText)
                .enqueue(enqueueSample)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        findViewById<EditText>(R.id.search_text_field).setText(savedInstanceState.getString(SEARCH_TEXT, SEARCH_TEXT_DEF))
    }

    private companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val SEARCH_TEXT_DEF = ""
    }

    fun clearRecyclerView(adapter: TrackAdapter) {
        adapter.clearList()
    }

    fun showMessage(textMessage: Int) {
        val placeholderMessage = findViewById<LinearLayout>(R.id.placeholderMessage)
        val errorImage = findViewById<ImageView>(R.id.errorImage)
        val errorMessage = findViewById<TextView>(R.id.errorMessage)
        val reloadButton = findViewById<Button>(R.id.reloadButton)
        when (textMessage) {
            R.string.nothing_was_found -> {
                reloadButton.visibility = View.GONE
                placeholderMessage.visibility = View.VISIBLE
                errorImage.setImageResource(R.drawable.nothing_was_found)
                errorMessage.setText(R.string.nothing_was_found)
            }
            R.string.communication_problems -> {
                placeholderMessage.visibility = View.VISIBLE
                errorImage.setImageResource(R.drawable.communication_problems)
                errorMessage.setText(R.string.communication_problems)
                reloadButton.visibility = View.VISIBLE
            }
            View.GONE -> {
                placeholderMessage.visibility = View.GONE
            }
        }
    }
}