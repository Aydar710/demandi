package com.aydar.demandi.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "rooms")
data class Room(
    @PrimaryKey
    var id: String = "",
    @ColumnInfo(name = "name")
    val name: String = "",
    @ColumnInfo(name = "subject_name")
    val subjectName: String = ""
) : Serializable