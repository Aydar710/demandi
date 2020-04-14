package com.aydar.featureroomdetails.domain

import com.aydar.demandi.data.model.Session
import com.aydar.demandi.data.repository.SessionRepository

class GetSessionsUseCase(private val sessionsRepository: SessionRepository) {

    suspend fun invoke(roomId: String): List<Session> {
        val sessions = sessionsRepository.getRoomSessions(roomId = roomId)
        return sessions
    }
}