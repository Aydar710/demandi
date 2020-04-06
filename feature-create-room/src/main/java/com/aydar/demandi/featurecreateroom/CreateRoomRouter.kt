package com.aydar.demandi.featurecreateroom

import androidx.appcompat.app.AppCompatActivity
import com.aydar.demandi.data.model.Room

interface CreateRoomRouter {

    fun moveToTeacherRoomActivity(activity: AppCompatActivity, room : Room)
}