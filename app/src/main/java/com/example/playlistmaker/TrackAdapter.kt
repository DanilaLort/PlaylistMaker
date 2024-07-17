package com.example.playlistmaker

import android.content.Context.MODE_PRIVATE
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val KEY_FOR_TRACK_HISTORY = "key_for_track_history"

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
            Log.d(
                "TRACK_LOG",
                "${tracks[position]}\n"
            )
            val savedHistory = Gson().fromJson<ArrayList<Track>>(
                sharedPrefs.getString(KEY_FOR_TRACK_HISTORY, ""),
                object : TypeToken<ArrayList<Track>>() {}.type
            )
            if (savedHistory.isNotEmpty() && savedHistory != null) tracksHistory.addAll(savedHistory)
            if (tracksHistory.contains(tracks[position])) tracksHistory.remove(tracks[position])
            tracksHistory.add(0, tracks[position])
            if (tracksHistory.size > 10) tracksHistory.removeAt(9)
            sharedPrefs.edit()
                .putString(KEY_FOR_TRACK_HISTORY, Gson().toJson(tracksHistory))
                .apply()
            Log.d(
                "TRACK_LOG",
                "$tracksHistory"
            )
        }
    }

    fun clearList() {
        val tracksSize = tracks.size
        tracks.clear()
        notifyItemRangeRemoved(0, tracksSize)
    }
}
