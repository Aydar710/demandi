package com.aydar.demandi.data.repository

import com.aydar.demandi.data.ROOMS_COLLECTION
import com.aydar.demandi.data.USERS_COLLECTION
import com.aydar.demandi.data.model.Room
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.tasks.await

class RoomsRepository(private val db: FirebaseFirestore) {

    fun addRoom(room: Room, userId: String = "testUserId") {
        val ref = db.collection(USERS_COLLECTION).document(userId)
        ref.collection(ROOMS_COLLECTION).document().set(room)
    }

    suspend fun getUserRooms(userId: String = "testUserId"): List<Room>? {
        val roomsSnapshot = db
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(ROOMS_COLLECTION)
            .get()
            .await()
        val rooms = roomsSnapshot.toObjects<Room>()
        return rooms
    }
}