package com.aydar.demandi.teacherrooms.presentation

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aydar.demandi.data.model.Room
import com.aydar.demandi.teacherrooms.TeacherRoomsRouter
import com.aydar.demandi.teacherrooms.domain.ShowRoomsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TeacherRoomsViewModel(
    private val router: TeacherRoomsRouter,
    private val showRoomsUseCase: ShowRoomsUseCase
) : ViewModel() {

    private val _roomsLiveData = MutableLiveData<List<Room>>()
    val roomsLiveData: LiveData<List<Room>>
        get() = _roomsLiveData

    fun onAddClicked(activity: AppCompatActivity) {
        router.moveToCreateRoomActivity(activity)
    }

    fun onRoomClicked(room: Room, activity: AppCompatActivity) {
        router.moveToRoomDetailsActivity(room, activity)
    }

    fun showRooms() {
        viewModelScope.launch(Dispatchers.IO) {
            val rooms = showRoomsUseCase.invoke()
            _roomsLiveData.postValue(rooms)
        }
    }
}