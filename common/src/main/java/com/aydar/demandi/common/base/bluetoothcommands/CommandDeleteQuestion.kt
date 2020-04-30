package com.aydar.demandi.common.base.bluetoothcommands

import com.aydar.demandi.data.model.Question
import java.io.Serializable

data class CommandDeleteQuestion(
    val question: Question
) : Serializable