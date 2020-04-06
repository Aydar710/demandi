package com.aydar.demandi.data.repository

import com.aydar.demandi.data.DAO.QuestionDao
import com.aydar.demandi.data.model.QuestionCache

class QuestionCacheRepository(private val database: QuestionDao) {

    fun saveQuestion(question: QuestionCache) {
        database.saveQuestion(question)
    }
}