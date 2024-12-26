package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.FavoriteTracksEntity

@Dao
interface FavoriteTracksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: FavoriteTracksEntity)
    @Delete(entity = FavoriteTracksEntity::class)
    suspend fun deleteTrack(entity: FavoriteTracksEntity)
    @Query("SELECT * FROM track_table ORDER BY date DESC;")
    suspend fun getTracks(): List<FavoriteTracksEntity>
    @Query("SELECT id FROM track_table")
    suspend fun getTracksId(): List<Int>
}
