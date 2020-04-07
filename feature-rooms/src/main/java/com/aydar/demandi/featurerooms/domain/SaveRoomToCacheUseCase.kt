package com.aydar.demandi.featurerooms.domain

import com.aydar.demandi.data.model.Room
import com.aydar.demandi.data.repository.RoomCacheRepository

class SaveRoomToCacheUseCase(private val roomCacheRepository: RoomCacheRepository) {

    suspend fun invoke(room: Room) {
        roomCacheRepository.saveRoom(room)
    }
}