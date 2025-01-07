package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.PlaylistTrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistTracksDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveTrack(track: PlaylistTrackEntity)
    @Query("SELECT * FROM playlist_track_table WHERE id IN (:ids)")
    fun getTracks(ids: List<Int>): Flow<List<PlaylistTrackEntity>>
    @Delete(entity = PlaylistTrackEntity::class)
    suspend fun deleteTrack(entity: PlaylistTrackEntity)
}