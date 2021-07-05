package com.aydar.featureteacherroom.domain

import com.aydar.demandi.data.model.Question
import com.aydar.demandi.data.repository.QuestionRepository
import com.google.firebase.auth.FirebaseUser

class SaveQuestionToFirestoreUseCase(
    private val questionRepository: QuestionRepository,
    private val user: FirebaseUser
) {

    suspend fun invoke(roomId: String, sessionId: String, question: Question) {
        questionRepository.saveQuestion(
            roomId = roomId,
            sessionId = sessionId,
            question = question,
            userId = user.uid
        )
    }
}