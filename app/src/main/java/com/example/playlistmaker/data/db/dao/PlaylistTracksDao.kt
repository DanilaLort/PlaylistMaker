package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.PlaylistTrackEntity

@Dao
interface PlaylistTracksDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveTrack(track: PlaylistTrackEntity)
    @Query("SELECT * FROM playlist_track_table WHERE Id = :ids")
    suspend fun getTracks(ids: List<Int>): List<PlaylistTrackEntity>
    @Delete(entity = PlaylistTrackEntity::class)
    suspend fun deleteTrack(entity: PlaylistTrackEntity)
}