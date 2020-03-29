package com.aydar.demandi.feature.room.teacher

import android.bluetooth.BluetoothSocket
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aydar.demandi.data.model.Question

class TeachersViewModel : ViewModel() {

    private val students = mutableListOf<BluetoothSocket>()

    private val _questionsLiveData = MutableLiveData<List<Question>>()

    val questionsLiveData: LiveData<List<Question>>
        get() = _questionsLiveData

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
    }
}