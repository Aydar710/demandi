package com.aydar.demandi.data.repository

import com.aydar.demandi.data.ROOMS_COLLECTION
import com.aydar.demandi.data.USERS_COLLECTION
import com.aydar.demandi.data.model.Room
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.tasks.await

class RoomsRepository(private val db: FirebaseFirestore) {

    suspend fun addRoom(room: Room, userId: String): String {
        val ref = db
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(ROOMS_COLLECTION)
            .document()

        val id = ref.id
        room.id = id

        ref.set(room)

        return id
    }

    suspend fun getUserRooms(userId: String): List<Room>? {
        val roomsSnapshot = db
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(ROOMS_COLLECTION)
            .get()
            .await()
        val rooms = roomsSnapshot.toObjects<Room>()
        return rooms
    }

    suspend fun getRoomById(roomId: String, userId: String): Room {
        val roomSnapshot = db
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(ROOMS_COLLECTION)
            .get()
            .await()

        val room = roomSnapshot.toObjects<Room>()
        return room[0]
    }
}