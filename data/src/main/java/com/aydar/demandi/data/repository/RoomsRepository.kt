package com.aydar.demandi.data.repository

import com.aydar.demandi.data.ROOMS_COLLECTION
import com.aydar.demandi.data.USERS_COLLECTION
import com.aydar.demandi.data.model.Room
import com.google.firebase.firestore.FirebaseFirestore

class RoomsRepository(private val db: FirebaseFirestore) {

    fun addRoom(room: Room, userId: String = "testUserId") {
        val ref = db.collection(USERS_COLLECTION).document(userId)
        ref.collection(ROOMS_COLLECTION).document().set(room)
    }
}