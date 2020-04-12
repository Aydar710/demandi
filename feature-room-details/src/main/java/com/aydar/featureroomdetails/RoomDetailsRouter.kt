package com.aydar.featureroomdetails

import androidx.appcompat.app.AppCompatActivity
import com.aydar.demandi.data.model.Room

interface RoomDetailsRouter {

    fun moveToTeachersRoomActivity(room: Room, activity: AppCompatActivity)
}