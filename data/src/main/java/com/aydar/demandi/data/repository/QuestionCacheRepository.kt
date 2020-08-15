package com.aydar.demandi.data.repository

import com.aydar.demandi.data.DAO.QuestionDao
import com.aydar.demandi.data.model.QuestionCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class QuestionCacheRepository(private val database: QuestionDao) {

    suspend fun saveQuestion(question: QuestionCache) {
        database.saveQuestion(question)
    }

    suspend fun getQuestions(roomId: String): List<QuestionCache> {
        val questions: List<QuestionCache> = withContext(Dispatchers.IO) {
            database.getQuestions(roomId)
        }
        return questions
    }
}