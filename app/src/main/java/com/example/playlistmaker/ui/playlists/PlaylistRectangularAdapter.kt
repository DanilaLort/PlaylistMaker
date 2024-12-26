package com.example.playlistmaker.ui.playlists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlaylistRectangularPanelBinding
import com.example.playlistmaker.databinding.PlaylistSquarePanelBinding
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track

class PlaylistRectangularAdapter(
    private val playlistClickListener: PlaylistClickListener
) : RecyclerView.Adapter<PlaylistRectangularViewHolder>() {
    var playlists: List<Playlist> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistRectangularViewHolder {
        val binding = PlaylistRectangularPanelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistRectangularViewHolder(binding)
    }

    override fun getItemCount(): Int = playlists.size

    override fun onBindViewHolder(holder: PlaylistRectangularViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            playlistClickListener.onPlaylistClick(playlists[position])
        }
    }

    fun interface PlaylistClickListener {
        fun onPlaylistClick(playlist: Playlist)
    }
}