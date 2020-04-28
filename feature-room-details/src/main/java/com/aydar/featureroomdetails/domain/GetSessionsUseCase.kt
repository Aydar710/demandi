package com.aydar.featureroomdetails.domain

import com.aydar.demandi.data.model.Session
import com.aydar.demandi.data.repository.SessionRepository
import com.google.firebase.auth.FirebaseUser

class GetSessionsUseCase(
    private val sessionsRepository: SessionRepository,
    private val user: FirebaseUser
) {

    suspend fun invoke(roomId: String): List<Session> {
        val sessions = sessionsRepository.getRoomSessions(roomId = roomId, userId = user.uid)
        return sessions
    }
}