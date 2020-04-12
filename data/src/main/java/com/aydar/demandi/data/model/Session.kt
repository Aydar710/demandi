package com.aydar.demandi.data.model

import java.util.*

data class Session(
    var id: String = "",
    val date: Date,
    val questions: List<Question> = listOf()
)