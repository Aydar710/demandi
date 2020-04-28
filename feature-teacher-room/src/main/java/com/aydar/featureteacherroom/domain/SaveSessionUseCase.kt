package com.aydar.featureteacherroom.domain

import com.aydar.demandi.data.model.Session
import com.aydar.demandi.data.repository.SessionRepository
import com.google.firebase.auth.FirebaseUser

class SaveSessionUseCase(
    private val sessionRepository: SessionRepository,
    private val user: FirebaseUser
) {

    fun invoke(session: Session, roomId: String): String {
        val sessionId = sessionRepository.saveSession(session, roomId, user.uid)
        return sessionId
    }
}