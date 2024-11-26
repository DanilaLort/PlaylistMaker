package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.TrackEntity

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)
    @Delete(entity = TrackEntity::class)
    suspend fun deleteTrack(entity: TrackEntity)
    @Query("SELECT * FROM track_table ORDER BY date DESC;")
    suspend fun getTracks(): List<TrackEntity>
    @Query("SELECT id FROM track_table")
    suspend fun getTracksId(): List<Int>
}
