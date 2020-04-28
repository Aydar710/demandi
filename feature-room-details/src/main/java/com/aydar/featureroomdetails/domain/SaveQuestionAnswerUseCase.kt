package com.aydar.featureroomdetails.domain

import com.aydar.demandi.data.model.Question
import com.aydar.demandi.data.model.Room
import com.aydar.demandi.data.model.Session
import com.aydar.demandi.data.repository.QuestionRepository
import com.google.firebase.auth.FirebaseUser

class SaveQuestionAnswerUseCase(
    private val questionRepository: QuestionRepository,
    private val user: FirebaseUser
) {

    suspend fun invoke(room: Room, session: Session, question: Question) {
        questionRepository.saveQuestionAnswer(
            roomId = room.id,
            session = session,
            question = question,
            userId = user.uid
        )
    }
}