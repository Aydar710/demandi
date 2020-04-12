package com.aydar.demandi.data.repository

import com.aydar.demandi.data.QUESTIONS_ARRAY
import com.aydar.demandi.data.ROOMS_COLLECTION
import com.aydar.demandi.data.SESSIONS_COLLECTION
import com.aydar.demandi.data.USERS_COLLECTION
import com.aydar.demandi.data.model.Question
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class QuestionRepository(private val db: FirebaseFirestore) {

    suspend fun saveQuestion(
        userId: String = "testUserId",
        roomId: String,
        sessionId: String,
        question: Question
    ) {
        db
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(ROOMS_COLLECTION)
            .document(roomId)
            .collection(SESSIONS_COLLECTION)
            .document(sessionId)
            .update(QUESTIONS_ARRAY, FieldValue.arrayUnion(question)).await()
    }
}