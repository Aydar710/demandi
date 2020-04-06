package com.aydar.demandi.featurerooms.student

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aydar.demandi.common.base.bluetooth.ServiceHolder
import com.aydar.demandi.data.model.Question
import com.aydar.demandi.data.model.Room

class StudentRoomViewModel : ViewModel() {

    lateinit var currentRoom: Room

    private val _questionsLiveData = MutableLiveData<List<Question>>()

    val questionsLiveData: LiveData<List<Question>>
        get() = _questionsLiveData

    init {
        _questionsLiveData.value = listOf()
    }

    fun sendQuestion(questionText: String) {
        ServiceHolder.studentService.sendQuestion(questionText)
    }

    fun addQuestion(question: Question) {
        val questions = _questionsLiveData.value?.toMutableList()
        questions?.add(question)
        _questionsLiveData.value = questions
    }

    fun addNewQuestions(questions : List<Question>){
        _questionsLiveData.postValue(questions)
    }

    fun onItemSwipedLeft(question: Question, position: Int) {
        deleteQuestion(question)
    }

    private fun deleteQuestion(question: Question) {

    }
}
