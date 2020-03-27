package com.aydar.demandi.featurecreateroom

import androidx.appcompat.app.AppCompatActivity

interface CreateRoomRouter {

    fun moveToTeacherRoomActivity(activity: AppCompatActivity, roomName: String)
}