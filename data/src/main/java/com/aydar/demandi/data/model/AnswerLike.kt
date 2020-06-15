package com.aydar.demandi.data.model

import java.io.Serializable

data class AnswerLike(
    val answerId: String,
    val userId: String
) : Serializable