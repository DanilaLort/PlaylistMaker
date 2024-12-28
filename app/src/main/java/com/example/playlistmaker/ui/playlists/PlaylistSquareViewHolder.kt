package com.example.playlistmaker.ui.playlists

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistSquarePanelBinding
import com.example.playlistmaker.domain.models.Playlist

class PlaylistSquareViewHolder(private val binding: PlaylistSquarePanelBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(model: Playlist) {
        binding.trackCount.text = model.getTrackCount()
        binding.playlistName.text = model.playlistName
        Glide.with(itemView)
            .load(model.coverPath)
            .placeholder(R.drawable.ic_cover)
            .centerCrop()
            .transform(RoundedCorners(8))
            .into(binding.playlistCover)
    }
}