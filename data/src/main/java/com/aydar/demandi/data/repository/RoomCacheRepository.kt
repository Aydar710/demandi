package com.aydar.demandi.data.repository

import com.aydar.demandi.data.DAO.RoomDao
import com.aydar.demandi.data.model.Room

class RoomCacheRepository(private val database: RoomDao) {

    fun saveRoom(room: Room) {
        database.saveRoom(room)
    }
}