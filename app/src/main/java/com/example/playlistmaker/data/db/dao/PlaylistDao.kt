package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.data.db.entity.PlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePlaylist(entity: PlaylistEntity)
    @Query("SELECT * FROM playlist_table ORDER BY date DESC;")
    fun getPlaylists(): Flow<List<PlaylistEntity>>
    @Query("SELECT * FROM playlist_table WHERE id = :id")
    fun getPlaylistById(id: Int): Flow<PlaylistEntity>
    @Query("SELECT trackList FROM playlist_table")
    suspend fun getPlaylistsTracksId(): List<String>
    @Update(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlaylist(entity: PlaylistEntity)
    @Delete(entity = PlaylistEntity::class)
    suspend fun deletePlaylist(entity: PlaylistEntity)
}