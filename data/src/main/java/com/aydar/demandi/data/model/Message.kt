package com.aydar.demandi.data.model

import java.io.Serializable

abstract class Message : Serializable {

    //TODO Вынести в константу
    val messageType: Int = 12345
}