package com.example.playlistmaker.ui.playlists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlaylistSquarePanelBinding
import com.example.playlistmaker.domain.models.Playlist

class PlaylistSquareAdapter(
    private val playlistClickListener: PlaylistClickListener
) : RecyclerView.Adapter<PlaylistSquareViewHolder>() {
    var playlists: List<Playlist> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistSquareViewHolder {
        val binding = PlaylistSquarePanelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistSquareViewHolder(binding)
    }

    override fun getItemCount(): Int = playlists.size

    override fun onBindViewHolder(holder: PlaylistSquareViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            playlistClickListener.onPlaylistClick(playlists[position])
        }
    }

    fun interface PlaylistClickListener {
        fun onPlaylistClick(playlist: Playlist)
    }
}