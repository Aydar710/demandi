package com.aydar.demandi.featurerooms.domain

import com.aydar.demandi.data.model.Session
import com.aydar.demandi.data.repository.SessionRepository

class SaveSessionUseCase(private val sessionRepository: SessionRepository) {

    fun invoke(session: Session, roomId: String): String {
        val sessionId = sessionRepository.saveSession(session, roomId)
        return sessionId
    }
}