package com.aydar.demandi.featurerooms.domain

import com.aydar.demandi.data.model.Question
import com.aydar.demandi.data.repository.QuestionRepository

class SaveQuestionToFirestoreUseCase(private val questionRepository: QuestionRepository) {

    suspend fun invoke(roomId: String, sessionId: String, question: Question) {
        questionRepository.saveQuestion(roomId = roomId, sessionId = sessionId, question = question)
    }
}