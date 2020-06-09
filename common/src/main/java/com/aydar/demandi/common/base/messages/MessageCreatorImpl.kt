package com.aydar.demandi.common.base.messages

import android.os.Handler
import android.os.Message

class MessageCreatorImpl(handler: Handler) : MessageCreator(handler) {

    override fun createMessage(
        message: com.aydar.demandi.data.model.Message
    ): Message {
        return handler.obtainMessage(
            message.messageType, message
        )
    }
}