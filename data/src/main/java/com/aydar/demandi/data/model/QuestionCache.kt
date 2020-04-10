package com.aydar.demandi.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "questions",
    foreignKeys = [ForeignKey(
        entity = Room::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("room_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class QuestionCache(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "text")
    val text: String,

    @ColumnInfo(name = "room_id")
    var roomId: String = ""
) : Serializable