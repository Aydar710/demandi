package com.aydar.demandi.teacherrooms.domain

import com.aydar.demandi.data.model.Room
import com.aydar.demandi.data.repository.RoomsRepository

class GetRoomUseCase(private val roomsRepository: RoomsRepository) {

    suspend fun invoke(roomId: String): Room {
        val room = roomsRepository.getRoomById(roomId)
        return room
    }
}