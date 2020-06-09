package com.aydar.demandi.data.model

import java.io.Serializable

data class AnswerLike(
    val answerId: String,
    val userId: String,
    override val messageType: Int = MESSAGE_RECEIVED_ANSWER_LIKE
) : Serializable, Message()