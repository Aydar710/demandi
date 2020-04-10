package com.aydar.demandi.featurerooms.domain

import com.aydar.demandi.data.model.Question
import com.aydar.demandi.data.model.QuestionCache
import com.aydar.demandi.data.model.Room
import com.aydar.demandi.data.repository.QuestionCacheRepository

class SaveQuestionToCacheUseCase(private val questionCacheRepository: QuestionCacheRepository) {

    suspend fun invoke(question: Question, room: Room) {
        val questionCache = QuestionCache(text = question.text, roomId = room.id)
        try {
            questionCacheRepository.saveQuestion(questionCache)
        }catch (e : Exception){
            e.printStackTrace()
        }
    }
}