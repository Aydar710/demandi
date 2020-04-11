package com.aydar.demandi.data.repository

import com.aydar.demandi.data.ROOMS_COLLECTION
import com.aydar.demandi.data.SESSIONS_COLLECTION
import com.aydar.demandi.data.USERS_COLLECTION
import com.aydar.demandi.data.model.Session
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SessionRepository(private val db: FirebaseFirestore) {

    fun saveSession(session: Session, roomId: String, userId: String = "testUserId"): String {
        val ref = db
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(ROOMS_COLLECTION)
            .document(roomId)
            .collection(SESSIONS_COLLECTION)
            .document()
        val sessionId = ref.id

        session.id = sessionId
        GlobalScope.launch {
            ref.set(session)
        }
        return sessionId
    }
}