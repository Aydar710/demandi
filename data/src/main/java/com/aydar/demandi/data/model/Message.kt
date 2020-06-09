package com.aydar.demandi.data.model

import java.io.Serializable

abstract class Message : Serializable {

    abstract val messageType: Int
}