package com.aydar.demandi.data.model

import java.io.Serializable
import java.util.*

data class Question(
    var id: String = Date().time.toString(),
    val text: String = "",
    var teacherAnswer: String = "",
    var studentAnswers: MutableList<Answer> = mutableListOf()
) : Serializable