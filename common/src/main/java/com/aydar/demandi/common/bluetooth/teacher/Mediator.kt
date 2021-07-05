package com.aydar.demandi.common.bluetooth.teacher

import com.aydar.demandi.data.model.Message

interface Mediator {

    fun sendMessage(message : Message, sender : TeacherConnectedThread)
}