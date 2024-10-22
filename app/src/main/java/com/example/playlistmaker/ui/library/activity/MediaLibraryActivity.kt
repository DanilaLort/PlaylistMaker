package com.example.playlistmaker.ui.library.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaLibraryBinding
import com.example.playlistmaker.ui.library.fragments.FavoriteTracksFragment
import com.example.playlistmaker.ui.library.fragments.MediaLibPagerAdapter
import com.example.playlistmaker.ui.library.fragments.PlaylistFragment
import com.google.android.material.tabs.TabLayoutMediator

class MediaLibraryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMediaLibraryBinding
    private lateinit var tabMediator: TabLayoutMediator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.medialibToMain.setOnClickListener {
            finish()
        }
        binding.mediaLibPager.adapter = MediaLibPagerAdapter(supportFragmentManager, lifecycle)
        tabMediator = TabLayoutMediator(binding.mediaLibTabLayout, binding.mediaLibPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favoriteTracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()
    }
}