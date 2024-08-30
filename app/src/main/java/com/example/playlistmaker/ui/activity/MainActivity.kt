package com.example.playlistmaker.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.playlistmaker.ui.theme.App
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (applicationContext as App).switchTheme(Creator.getThemeManager(this).getValue())
        findViewById<Button>(R.id.button_search).setOnClickListener {
            startActivity(
                Intent(
                    this,
                    SearchActivity::class.java
                )
            )
        }
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