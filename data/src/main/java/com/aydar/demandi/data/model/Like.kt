package com.aydar.demandi.data.model

import java.io.Serializable

data class Like(
    val questionId: String,
    val count: Long
) : Serializable