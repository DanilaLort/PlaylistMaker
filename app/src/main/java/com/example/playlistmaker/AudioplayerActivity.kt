package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson

class AudioPlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        val track = Gson().fromJson<Track>(intent.getStringExtra("track"), Track::class.java)

        findViewById<ImageButton>(R.id.returnButton).setOnClickListener {
            finish()
        }
        findViewById<ImageView>(R.id.trackCover).setImageResource(R.drawable.ic_cover)
        findViewById<TextView>(R.id.trackTittle).text = track.trackName
        findViewById<TextView>(R.id.artistName).text = track.artistName
        findViewById<TextView>(R.id.trackDuration).text = track.getTrackTime()
        findViewById<TextView>(R.id.trackAlbum).text = track.collectionName
        findViewById<TextView>(R.id.trackGenre).text = track.primaryGenreName
        findViewById<TextView>(R.id.trackYear).text = track.releaseDate.substring(0, 4)
        findViewById<TextView>(R.id.trackCountry).text = track.country
        Glide.with(this)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.ic_cover)
            .transform(RoundedCorners(16))
            .into(findViewById<ImageView>(R.id.trackCover))
    }
}