package com.aydar.demandi.teacherrooms.domain

import com.aydar.demandi.data.model.Room
import com.aydar.demandi.data.repository.RoomsRepository
import com.google.firebase.auth.FirebaseUser

class ShowRoomsUseCase(
    private val roomsRepository: RoomsRepository,
    private val user: FirebaseUser
) {

    suspend fun invoke(): List<Room>? {
        val rooms = roomsRepository.getUserRooms(user.uid)
        return rooms
    }
}