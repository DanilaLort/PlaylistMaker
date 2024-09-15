package com.example.playlistmaker.ui.main.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.ui.library.activity.MediaLibraryActivity
import com.example.playlistmaker.ui.main.view_model.MainActivityViewModel
import com.example.playlistmaker.ui.search.activity.SearchActivity
import com.example.playlistmaker.ui.settings.activity.SettingsActivity

private var settings = false

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: ActivityMainBinding
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, MainActivityViewModel.getViewModelFactory())[MainActivityViewModel::class.java]
        viewModel.updateThemeState()
        viewModel.getThemeStateLiveData().observe(this) { themeState ->
            if(!settings) (applicationContext as App).switchTheme(themeState)
        }
        binding.buttonSearch.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    SearchActivity::class.java
                )
            )
        }
        binding.buttonMediaLib.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    MediaLibraryActivity::class.java
                )
            )
        }
        binding.buttonSettings.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    SettingsActivity::class.java
                )
            )
            settings = true
        }
    }
}