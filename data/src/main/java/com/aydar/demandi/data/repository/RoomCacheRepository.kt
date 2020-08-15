package com.aydar.demandi.data.repository

import com.aydar.demandi.data.dao.RoomDao
import com.aydar.demandi.data.model.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoomCacheRepository(private val database: RoomDao) {

    suspend fun saveRoom(room: Room) {
        try {
            database.saveRoom(room)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getRoomById(roomId: String): Room? {
        val room: Room? = withContext(Dispatchers.IO) {
            database.getRoomById(roomId)
        }
        return room
    }
}