package com.aydar.demandi.data.DAO

import androidx.room.Dao
import androidx.room.Insert
import com.aydar.demandi.data.model.Room

@Dao
interface RoomDao {

    @Insert
    fun saveRoom(room: Room)
}