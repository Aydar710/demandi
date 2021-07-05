package com.aydar.demandi.featurecreateroom.presentation

import com.aydar.demandi.data.model.Room
import kotlinx.android.synthetic.main.activity_create_room.*

class CreateRoomViewHolder(private val activity: CreateRoomActivity) {

    fun getRoom(): Room {
        with(activity) {
            return Room(name = et_name.text.toString(), subjectName = et_subject_name.text.toString())
        }
    }
}