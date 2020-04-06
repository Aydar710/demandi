package com.aydar.demandi.featurecreateroom.domain

import com.aydar.demandi.data.model.Room
import com.aydar.demandi.data.repository.RoomsRepository

class CreateRoomUseCase(private val roomsRepository: RoomsRepository) {

    fun invoke(room: Room): String {
        val roomId = roomsRepository.addRoom(room)
        return roomId
    }
}