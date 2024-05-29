package com.example.playlistmaker

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatDelegate

class SearchActivity : AppCompatActivity() {
    private var searchText = SEARCH_TEXT_DEF
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        findViewById<Button>(R.id.search_to_main).setOnClickListener {
            finish()
        }
        val editText = findViewById<EditText>(R.id.search_text_field)
        val buttonClear = findViewById<Button>(R.id.button_clear)
        buttonClear.visibility = View.GONE
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    buttonClear.visibility = View.VISIBLE
                } else {
                    buttonClear.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {
                searchText = editText.text.toString()
            }
        }
        editText.addTextChangedListener(textWatcher)
        buttonClear.setOnClickListener {
            editText.setText("")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(SEARCH_TEXT, SEARCH_TEXT_DEF)
    }

    override fun onResume() {
        super.onResume()
        findViewById<EditText>(R.id.search_text_field).setText(searchText)
    }
    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val SEARCH_TEXT_DEF = ""
    }
}