package com.aydar.demandi.common.base.bluetooth

import com.aydar.demandi.data.model.Message

interface Mediator {

    fun sendMessage(message : Message, sender : ConnectedThread)
}