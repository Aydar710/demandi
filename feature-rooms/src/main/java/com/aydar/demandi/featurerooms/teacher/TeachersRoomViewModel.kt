package com.aydar.demandi.featurerooms.teacher

import android.bluetooth.BluetoothSocket
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aydar.demandi.data.model.Question
import com.aydar.demandi.data.model.Room
import com.aydar.demandi.data.model.Session
import com.aydar.demandi.featurerooms.domain.SaveQuestionToFirestoreUseCase
import com.aydar.demandi.featurerooms.domain.SaveSessionUseCase
import kotlinx.coroutines.launch
import java.util.*

class TeachersRoomViewModel(
    private val saveQuestionToFirestoreUseCase: SaveQuestionToFirestoreUseCase,
    private val saveSessionUseCase: SaveSessionUseCase
) :
    ViewModel() {

    lateinit var room: Room

    private val students = mutableListOf<BluetoothSocket>()

    private val _questionsLiveData = MutableLiveData<List<Question>>()

    val questionsLiveData: LiveData<List<Question>>
        get() = _questionsLiveData
    private lateinit var session: Session

    init {
        _questionsLiveData.value = listOf()
    }

    fun addStudent(socket: BluetoothSocket) {
        students.add(socket)
    }

    fun addQuestion(question: Question) {
        val questions = _questionsLiveData.value?.toMutableList()
        questions?.add(question)
        _questionsLiveData.value = questions
        viewModelScope.launch {
            saveQuestionToFirestoreUseCase.invoke(room.id, session.id, question)
        }
    }

    fun saveSession() {
        session = Session(date = Date())
        val sessionId = saveSessionUseCase.invoke(session, room.id)
        session.id = sessionId
    }
}