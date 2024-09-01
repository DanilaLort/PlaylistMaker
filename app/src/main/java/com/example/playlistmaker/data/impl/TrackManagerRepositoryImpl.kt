package com.example.playlistmaker.data.impl

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.ValueManagerRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
class TrackManagerRepositoryImpl(context: Context) : ValueManagerRepository<List<Track>> {
    private val sharedPrefs = context.getSharedPreferences(SHARED_PREFERENCES, AppCompatActivity.MODE_PRIVATE)
    private val gson = Gson()
    private val trackListType = object : TypeToken<ArrayList<Track>>() {}.type
   override fun saveValue(tracks: List<Track>) {
        sharedPrefs.edit()
            .putString(KEY_FOR_TRACK_HISTORY, Gson().toJson(tracks))
            .apply()
   }

    override fun getValue() : List<Track> {
        return gson.fromJson(
            sharedPrefs.getString(
                KEY_FOR_TRACK_HISTORY, SEARCH_HISTORY_DEF_VALUE
            ), trackListType)
    }

    private companion object {
        const val SHARED_PREFERENCES = "preference_for_tracks"
        const val KEY_FOR_TRACK_HISTORY = "key_for_track_history"
        const val SEARCH_HISTORY_DEF_VALUE = "[]"
    }
}