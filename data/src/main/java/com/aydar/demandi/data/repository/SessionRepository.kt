package com.aydar.demandi.data.repository

import com.aydar.demandi.data.ROOMS_COLLECTION
import com.aydar.demandi.data.SESSIONS_COLLECTION
import com.aydar.demandi.data.USERS_COLLECTION
import com.aydar.demandi.data.model.Session
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.tasks.await

class SessionRepository(private val db: FirebaseFirestore) {

    suspend fun saveSession(session: Session, roomId: String, userId: String): String {
        val ref = db
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(ROOMS_COLLECTION)
            .document(roomId)
            .collection(SESSIONS_COLLECTION)
            .document()
        val sessionId = ref.id

        session.id = sessionId
        ref.set(session)
        return sessionId
    }

    suspend fun getRoomSessions(userId: String, roomId: String): List<Session> {
        val sessionsSnapshot = db
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(ROOMS_COLLECTION)
            .document(roomId)
            .collection(SESSIONS_COLLECTION)
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .await()

        val sessions = sessionsSnapshot.toObjects<Session>()
        return sessions
    }
}