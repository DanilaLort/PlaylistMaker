package com.example.playlistmaker.data.impl

import android.content.SharedPreferences
import com.example.playlistmaker.domain.api.ValueManagerRepository
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TrackManagerRepositoryImpl(
    private val sharedPrefs: SharedPreferences,
    private val gson: Gson
) : ValueManagerRepository<List<Track>> {
//    private val sharedPrefs = context.getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
//    private val gson = Gson()
    private val trackListType = object : TypeToken<ArrayList<Track>>() {}.type
   override fun saveValue(value: List<Track>) {
        sharedPrefs.edit()
            .putString(KEY_FOR_TRACK_HISTORY, gson.toJson(value))
            .apply()
   }

    override fun getValue() : List<Track> {
        return gson.fromJson(
            sharedPrefs.getString(
                KEY_FOR_TRACK_HISTORY, SEARCH_HISTORY_DEF_VALUE
            ), trackListType)
    }

    private companion object {
        const val KEY_FOR_TRACK_HISTORY = "key_for_track_history"
        const val SEARCH_HISTORY_DEF_VALUE = "[]"
    }
}