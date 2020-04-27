package com.aydar.demandi.data.model

import java.io.Serializable

data class Question(
    val id: String = "",
    var text: String = "",
    var likeCount: Long = 0,
    var teacherAnswer: String = "",
    var studentAnswers: MutableList<Answer> = mutableListOf()
) : Serializable