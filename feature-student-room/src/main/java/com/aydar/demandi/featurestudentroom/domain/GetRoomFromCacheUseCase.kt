package com.aydar.demandi.featurestudentroom.domain

import com.aydar.demandi.data.model.Room
import com.aydar.demandi.data.repository.RoomCacheRepository

class GetRoomFromCacheUseCase(private val roomCacheRepository: RoomCacheRepository) {

    suspend fun invoke(roomId: String): Room? {
        val room = roomCacheRepository.getRoomById(roomId)
        return room
    }
}