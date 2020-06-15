package com.aydar.demandi.common.base.bluetoothmessages

import com.aydar.demandi.data.model.Message
import com.aydar.demandi.data.model.Question
import java.io.Serializable

data class MessageDeleteQuestion(
    val question: Question
) : Serializable, Message()