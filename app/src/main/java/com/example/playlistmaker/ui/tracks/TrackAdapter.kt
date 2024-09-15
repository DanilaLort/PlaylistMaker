package com.example.playlistmaker.ui.tracks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track


object Delay {
    const val ONE_SECOND_DELAY = 1000L
    const val TWO_SECOND_DELAY = 2000L
}


class TrackAdapter(
    private val clickListener: TrackClickListener
) : RecyclerView.Adapter<TrackViewHolder> () {
    var tracks: List<Track> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_panel, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int = tracks.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener { clickListener.onTrackClick(tracks[position])
        }
    }

    fun clearList() {
        val tracksSize = tracks.size
        (tracks as ArrayList).clear()
        notifyItemRangeRemoved(0, tracksSize)
    }

    fun interface TrackClickListener {
        fun onTrackClick(track: Track)
    }
}
