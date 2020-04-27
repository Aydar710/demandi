package com.aydar.featureroomdetails

import androidx.appcompat.app.AppCompatActivity
import com.aydar.demandi.data.model.Room

interface RoomDetailsRouter {

    fun moveToTeacherRoomActivity(room: Room, activity: AppCompatActivity)
}