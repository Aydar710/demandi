package com.aydar.demandi.featurecreateroom.presentation

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.aydar.demandi.common.base.bluetooth.ServiceHolder
import com.aydar.demandi.data.model.Room
import com.aydar.demandi.featurecreateroom.CreateRoomRouter
import com.aydar.demandi.featurecreateroom.domain.CreateRoomUseCase

class CreateRoomViewModel(
    private val router: CreateRoomRouter,
    private val createRoomUseCase: CreateRoomUseCase
) : ViewModel() {

    fun onCreateBtnClicked(room: Room, activity: AppCompatActivity) {
        ServiceHolder.teacherService.startServer()
        ServiceHolder.teacherService.room = room
        createRoom(room, activity)
    }

    private fun createRoom(room: Room, activity: AppCompatActivity) {
        val roomId = createRoomUseCase.invoke(room)
        room.id = roomId
        router.moveToTeacherRoomActivity(activity, room)
    }
}