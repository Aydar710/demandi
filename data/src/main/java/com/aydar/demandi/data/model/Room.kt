package com.aydar.demandi.data.model

import java.io.Serializable

data class Room(
    var id: String = "",
    val name: String = "",
    val subjectName: String = ""
) : Serializable