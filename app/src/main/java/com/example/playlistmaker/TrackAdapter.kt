package com.example.playlistmaker

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable

const val KEY_FOR_TRACK_HISTORY = "key_for_track_history"
const val SEARCH_HISTORY_DEF_VALUE = "[]"
const val SEARCH_HISTORY_SIZE = 10

class TrackAdapter(
    private val tracks: ArrayList<Track>
) : RecyclerView.Adapter<TrackViewHolder> ()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_panel, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int = tracks.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        val sharedPrefs = holder.itemView.context.getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
        holder.itemView.setOnClickListener {
            val tracksHistory: ArrayList<Track> = ArrayList()
            val savedHistory = Gson().fromJson<ArrayList<Track>>(
                sharedPrefs.getString(KEY_FOR_TRACK_HISTORY, SEARCH_HISTORY_DEF_VALUE),
                object : TypeToken<ArrayList<Track>>() {}.type
            )
            if (savedHistory.isNotEmpty() && savedHistory != null) tracksHistory.addAll(savedHistory)
            if (tracksHistory.contains(tracks[position])) tracksHistory.remove(tracks[position])
            tracksHistory.add(0, tracks[position])
            if (tracksHistory.size > SEARCH_HISTORY_SIZE) tracksHistory.removeAt(SEARCH_HISTORY_SIZE - 1)
            sharedPrefs.edit()
                .putString(KEY_FOR_TRACK_HISTORY, Gson().toJson(tracksHistory))
                .apply()
            val intent = Intent(
                holder.itemView.context,
                AudioPlayerActivity::class.java
            )
            intent.putExtra("track", Gson().toJson(tracks[position]))
            holder.itemView.context.startActivity(
                intent
            )
        }
    }

    fun clearList() {
        val tracksSize = tracks.size
        tracks.clear()
        notifyItemRangeRemoved(0, tracksSize)
    }
}
