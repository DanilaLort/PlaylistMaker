package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.FavoriteTracksEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteTracksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: FavoriteTracksEntity)
    @Delete(entity = FavoriteTracksEntity::class)
    suspend fun deleteTrack(entity: FavoriteTracksEntity)
    @Query("SELECT * FROM track_table ORDER BY date DESC;")
    fun getTracks(): Flow<List<FavoriteTracksEntity>>
    @Query("SELECT id FROM track_table")
    fun getTracksId(): Flow<List<Int>>
}
