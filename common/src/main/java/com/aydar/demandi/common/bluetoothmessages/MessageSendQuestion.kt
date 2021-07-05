package com.aydar.demandi.common.bluetoothmessages

import com.aydar.demandi.data.model.Message
import com.aydar.demandi.data.model.Question

class MessageSendQuestion(
    val question: Question
) : Message()