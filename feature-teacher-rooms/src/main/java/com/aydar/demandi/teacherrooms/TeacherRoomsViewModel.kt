package com.aydar.demandi.teacherrooms

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.aydar.demandi.data.repository.RoomsRepository

class TeacherRoomsViewModel(
    private val repo: RoomsRepository,
    private val router: TeacherRoomsRouter
) : ViewModel() {

    fun onAddClicked(activity: AppCompatActivity) {
        router.moveToCreateRoomActivity(activity)
    }
}