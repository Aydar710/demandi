package com.aydar.demandi.featurerooms.student

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aydar.demandi.common.base.bluetooth.ServiceHolder
import com.aydar.demandi.data.model.Question
import com.aydar.demandi.data.model.Room
import com.aydar.demandi.featurerooms.domain.SaveQuestionToCacheUseCase
import kotlinx.coroutines.launch

class StudentRoomViewModel(private val saveQuestionToCacheUseCase: SaveQuestionToCacheUseCase) :
    ViewModel() {

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
        saveQuestionToCache(question)
    }

    fun addNewQuestions(questions: List<Question>) {
        _questionsLiveData.postValue(questions)
    }

    fun onItemSwipedLeft(question: Question, position: Int) {
        deleteQuestion(question)
    }

    private fun saveQuestionToCache(question: Question) {
        viewModelScope.launch {
            saveQuestionToCacheUseCase.invoke(question, currentRoom)
        }
    }

    private fun deleteQuestion(question: Question) {

    }

}
