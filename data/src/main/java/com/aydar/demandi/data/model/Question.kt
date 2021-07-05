package com.aydar.demandi.data.model

import java.io.Serializable

data class Question(
    val id: String = "",
    val text: String = "",
    var teacherAnswer: String = "",
    var studentAnswers: MutableList<Answer> = mutableListOf(),
    var likes: MutableList<QuestionLike> = mutableListOf(),
    var visibleToOthers: Boolean = false
) : Serializable