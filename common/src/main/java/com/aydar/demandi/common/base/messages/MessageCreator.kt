package com.aydar.demandi.common.base.messages

import android.os.Handler
import com.aydar.demandi.data.model.Message

abstract class MessageCreator(protected val handler: Handler) {

    abstract fun createMessage(message: Message): android.os.Message
}