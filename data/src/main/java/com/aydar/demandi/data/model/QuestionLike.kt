package com.aydar.demandi.data.model

import java.io.Serializable

data class QuestionLike(
    val questionId: String,
    val userId: String,
    override val messageType: Int = MESSAGE_RECEIVED_QUESTION_LIKE
) : Serializable, Message()