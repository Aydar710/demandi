package com.aydar.demandi.data.model

import java.io.Serializable
import java.util.*

data class Answer(
    val text: String = "",
    val questionId: String = "",
    val userId: String,
    val id: String = Date().time.toString(),
    val likes: MutableList<AnswerLike> = mutableListOf(),
    override val messageType: Int = MESSAGE_RECEIVED_ANSWER
) : Serializable, Message()