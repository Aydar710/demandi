package com.aydar.demandi.teacherrooms.domain

import com.aydar.demandi.data.model.Room
import com.aydar.demandi.data.repository.RoomsRepository

class ShowRoomsUseCase(private val roomsRepository: RoomsRepository) {

    suspend fun invoke(userId: String = "testUserId"): List<Room>? {
        val rooms = roomsRepository.getUserRooms()
        return rooms
    }
}