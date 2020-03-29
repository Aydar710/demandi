package com.aydar.demandi.feature.room.student

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aydar.demandi.data.model.Question

class StudentsViewModel : ViewModel() {

    private val _questionsLiveData = MutableLiveData<List<Question>>()

    val questionsLiveData: LiveData<List<Question>>
        get() = _questionsLiveData

    init {
        _questionsLiveData.value = listOf()
    }

    fun addQuestion(question: Question) {
        val questions = _questionsLiveData.value?.toMutableList()
        questions?.add(question)
        _questionsLiveData.value = questions
    }
}
