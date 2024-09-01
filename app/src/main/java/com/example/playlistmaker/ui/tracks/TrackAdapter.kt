package com.example.playlistmaker.ui.tracks

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.activity.AudioPlayerActivity
import com.example.playlistmaker.ui.activity.TRACK_INTENT_VALUE
import com.google.gson.Gson

const val SEARCH_HISTORY_SIZE = 10

object Delay {
    const val ONE_SECOND_DELAY = 1000L
    const val TWO_SECOND_DELAY = 2000L
}

val handler = Handler(Looper.getMainLooper())


class TrackAdapter(
    private val clickListener: TrackClickListener
) : RecyclerView.Adapter<TrackViewHolder> () {
    var tracks = ArrayList<Track>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_panel, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int = tracks.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener { clickListener.onTrackClick(tracks, position)
        }
    }

    fun clearList() {
        val tracksSize = tracks.size
        tracks.clear()
        notifyItemRangeRemoved(0, tracksSize)
    }

    fun interface TrackClickListener {
        fun onTrackClick(tracks: ArrayList<Track>, position: Int)
    }
}
