package com.aydar.demandi.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.aydar.demandi.data.model.Room

@Dao
interface RoomDao {

    @Insert
    suspend fun saveRoom(room: Room)

    @Query("SELECT * FROM rooms WHERE id  = :roomId")
    suspend fun getRoomById(roomId: String): Room
}