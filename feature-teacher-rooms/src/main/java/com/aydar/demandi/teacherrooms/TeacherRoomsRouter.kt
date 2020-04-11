package com.aydar.demandi.teacherrooms

import androidx.appcompat.app.AppCompatActivity
import com.aydar.demandi.data.model.Room

interface TeacherRoomsRouter {

    fun moveToCreateRoomActivity(activity: AppCompatActivity)
    fun moveToRoomDetailsActivity(room: Room, activity: AppCompatActivity)
}