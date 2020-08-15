package com.aydar.demandi.teacherrooms.presentation

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aydar.demandi.common.SingleLiveEvent
import com.aydar.demandi.data.model.Room
import com.aydar.demandi.teacherrooms.TeacherRoomsCommands
import com.aydar.demandi.teacherrooms.TeacherRoomsRouter
import com.aydar.demandi.teacherrooms.domain.ShowRoomsUseCase
import kotlinx.coroutines.launch

class TeacherRoomsViewModel(
    private val router: TeacherRoomsRouter,
    private val showRoomsUseCase: ShowRoomsUseCase
) : ViewModel() {

    private val _roomsLiveData = MutableLiveData<List<Room>>()
    val roomsLiveData: LiveData<List<Room>>
        get() = _roomsLiveData

    private val _command = SingleLiveEvent<TeacherRoomsCommands>()
    val command: LiveData<TeacherRoomsCommands>
        get() = _command

    fun onAddClicked(activity: AppCompatActivity) {
        router.moveToCreateRoomActivity(activity)
    }

    fun onRoomClicked(room: Room, activity: AppCompatActivity) {
        router.moveToRoomDetailsActivity(room, activity)
    }

    fun showRooms() {
        _command.value = TeacherRoomsCommands.ShowProgress
        viewModelScope.launch {
            _command.postValue(TeacherRoomsCommands.HideProgress)
            val rooms = showRoomsUseCase.invoke()
            _roomsLiveData.postValue(rooms)
            rooms?.let {
                if (it.isNullOrEmpty()) {
                    _command.postValue(TeacherRoomsCommands.HasNoRooms)
                }
            }
        }
    }
}