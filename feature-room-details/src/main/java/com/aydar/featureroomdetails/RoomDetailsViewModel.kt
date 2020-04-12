package com.aydar.featureroomdetails

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.aydar.demandi.common.base.bluetooth.ServiceHolder
import com.aydar.demandi.data.model.Room

class RoomDetailsViewModel(private val router: RoomDetailsRouter) : ViewModel() {

    fun openRoom(room: Room, activity : AppCompatActivity) {
        ServiceHolder.teacherService.startServer()
        ServiceHolder.teacherService.room = room
        router.moveToTeachersRoomActivity(room, activity)
    }
}