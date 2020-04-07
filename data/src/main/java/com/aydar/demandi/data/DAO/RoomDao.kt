package com.aydar.demandi.data.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.aydar.demandi.data.model.Room

@Dao
interface RoomDao {

    @Insert
    fun saveRoom(room: Room)

    @Query("SELECT * FROM rooms WHERE id  = :roomId")
    fun getRoomById(roomId: String): Room
}