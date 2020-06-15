package com.aydar.demandi.data.model

import java.io.Serializable

data class QuestionLike(
    val questionId: String,
    val userId: String
) : Serializable