package com.aydar.demandi.teacherrooms.domain

import com.aydar.demandi.data.model.Room
import com.aydar.demandi.data.repository.RoomsRepository
import com.google.firebase.auth.FirebaseUser

class GetRoomUseCase(private val roomsRepository: RoomsRepository, private val user: FirebaseUser) {

    suspend fun invoke(roomId: String): Room {
        val room = roomsRepository.getRoomById(roomId, user.uid)
        return room
    }
}