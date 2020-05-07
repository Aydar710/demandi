package com.aydar.demandi.featurestudentroom.domain.usecase

import com.aydar.demandi.data.model.Question
import com.aydar.demandi.data.repository.QuestionCacheRepository

class GetCachedQuestionsUseCase(private val questionCacheRepository: QuestionCacheRepository) {

    suspend fun invoke(roomId: String): List<Question> {
        val questions = questionCacheRepository.getQuestions(roomId)
        return questions.map {
            Question(text = it.text)
        }
    }
}