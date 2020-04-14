package com.aydar.demandi.data.model

import java.util.*

data class Session(
    var id: String = "",
    val date: Date = Date(),
    var questions: List<Question> = listOf()
)