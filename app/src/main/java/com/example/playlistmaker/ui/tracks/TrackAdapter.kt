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

private var isClickAllowed = true

val handler = Handler(Looper.getMainLooper())

private fun clickDebounce() : Boolean {
    val current = isClickAllowed
    if (isClickAllowed) {
        isClickAllowed = false
        handler.postDelayed({ isClickAllowed = true }, Delay.ONE_SECOND_DELAY)
    }
    return current
}

class TrackAdapter(
    private val tracks: ArrayList<Track>
) : RecyclerView.Adapter<TrackViewHolder> ()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_panel, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int = tracks.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val trackManager = Creator.getTrackManager(holder.itemView.context)
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            if (clickDebounce()) {
                val tracksHistory: ArrayList<Track> = ArrayList()
                val savedHistory = trackManager.getValue()
                if (savedHistory.isNotEmpty() && savedHistory != null) tracksHistory.addAll(
                    savedHistory
                )
                if (tracksHistory.contains(tracks[position])) tracksHistory.remove(tracks[position])
                tracksHistory.add(0, tracks[position])
                if (tracksHistory.size > SEARCH_HISTORY_SIZE) tracksHistory.removeAt(
                    SEARCH_HISTORY_SIZE - 1
                )
                trackManager.saveValue(tracksHistory)
                val intent = Intent(
                    holder.itemView.context,
                    AudioPlayerActivity::class.java
                )
                intent.putExtra(TRACK_INTENT_VALUE, Gson().toJson(tracks[position]))
                holder.itemView.context.startActivity(
                    intent
                )
            }
        }
    }

    fun clearList() {
        val tracksSize = tracks.size
        tracks.clear()
        notifyItemRangeRemoved(0, tracksSize)
    }
}
