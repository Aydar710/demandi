package com.aydar.featureroomdetails.presentation

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aydar.demandi.common.base.bluetooth.TeacherBluetoothServiceMediator
import com.aydar.demandi.data.model.Question
import com.aydar.demandi.data.model.Room
import com.aydar.demandi.data.model.Session
import com.aydar.featureroomdetails.RoomDetailsRouter
import com.aydar.featureroomdetails.domain.GetSessionsUseCase
import com.aydar.featureroomdetails.domain.SaveQuestionAnswerUseCase
import kotlinx.coroutines.launch

class RoomDetailsViewModel(
    private val router: RoomDetailsRouter,
    private val getSessionsUseCase: GetSessionsUseCase,
    private val saveQuestionAnswerUseCase: SaveQuestionAnswerUseCase,
    private val teacherService: TeacherBluetoothServiceMediator
) : ViewModel() {

    lateinit var currentRoom: Room

    private val _sessionLiveData = MutableLiveData<List<Session>>()
    val sessionLiveData: LiveData<List<Session>>
        get() = _sessionLiveData

    fun openRoom(room: Room, activity: AppCompatActivity) {
        teacherService.startRoomServer(room)
        router.moveToTeacherRoomActivity(room, activity)
    }

    fun getSessions() {
        viewModelScope.launch {
            try {
                val sessions = getSessionsUseCase.invoke(currentRoom.id)
                _sessionLiveData.postValue(sessions)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun saveQuestionAnswer(room: Room, session: Session, question: Question) {
        viewModelScope.launch {
            try {
                saveQuestionAnswerUseCase.invoke(room, session, question)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}