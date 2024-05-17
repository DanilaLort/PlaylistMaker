package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button_search = findViewById<Button>(R.id.button_search)
        val displayIntent = Intent(this, SearchActivity::class.java)
        val buttonClickListener: View.OnClickListener = View.OnClickListener { startActivity(displayIntent) }
        button_search.setOnClickListener(buttonClickListener)
        findViewById<Button>(R.id.button_media_lib).setOnClickListener {
            startActivity(
                Intent(
                    this,
                    MediaLibraryActivity::class.java
                )
            )
        }
        findViewById<Button>(R.id.button_settings).setOnClickListener {
            startActivity(
                Intent(
                    this,
                    SettingsActivity::class.java
                )
            )
        }
    }
}