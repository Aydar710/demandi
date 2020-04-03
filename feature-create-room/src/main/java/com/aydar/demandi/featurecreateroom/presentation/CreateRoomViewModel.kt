package com.aydar.demandi.featurecreateroom.presentation

import androidx.lifecycle.ViewModel
import com.aydar.demandi.data.model.Room
import com.aydar.demandi.featurecreateroom.domain.CreateRoomUseCase

class CreateRoomViewModel(private val createRoomUseCase: CreateRoomUseCase) : ViewModel() {

    fun createRoom(room: Room) {
        createRoomUseCase.invoke(room)
    }
}