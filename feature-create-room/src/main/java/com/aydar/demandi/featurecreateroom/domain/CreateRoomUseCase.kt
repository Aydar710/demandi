package com.aydar.demandi.featurecreateroom.domain

import com.aydar.demandi.data.model.Room
import com.aydar.demandi.data.repository.RoomsRepository
import com.google.firebase.auth.FirebaseUser

class CreateRoomUseCase(
    private val roomsRepository: RoomsRepository,
    private val user: FirebaseUser
) {

    fun invoke(room: Room): String {
        val roomId = roomsRepository.addRoom(room, user.uid)
        return roomId
    }
}