package com.aydar.demandi.data.model

import java.io.Serializable

data class Question(
    var id: String = "",
    val text: String = "",
    var answer : String = ""
) : Serializable