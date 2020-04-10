package com.aydar.demandi.featurerooms.student

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aydar.demandi.common.base.bluetooth.ServiceHolder
import com.aydar.demandi.data.model.Question
import com.aydar.demandi.data.model.Room
import com.aydar.demandi.featurerooms.domain.GetCachedQuestionsUseCase
import com.aydar.demandi.featurerooms.domain.GetRoomFromCacheUseCase
import com.aydar.demandi.featurerooms.domain.SaveQuestionToCacheUseCase
import com.aydar.demandi.featurerooms.domain.SaveRoomToCacheUseCase
import kotlinx.coroutines.launch

class StudentRoomViewModel(
    private val saveQuestionToCacheUseCase: SaveQuestionToCacheUseCase,
    private val saveRoomToCacheUseCase: SaveRoomToCacheUseCase,
    private val getRoomFromCacheUseCase: GetRoomFromCacheUseCase,
    private val getCachedQuestionsUseCase: GetCachedQuestionsUseCase
) :
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

    fun handleReceivedRoom(room: Room) {
        currentRoom = Room(room.id, room.name, room.subjectName)
        viewModelScope.launch {
            val roomFromDb = getRoomFromCacheUseCase.invoke(room.id)
            if (roomFromDb == null) {
                saveRoomToCache(room)
            } else {
                showRoomQuestions(room)
            }
        }
    }

    private fun saveRoomToCache(room: Room) {
        viewModelScope.launch {
            saveRoomToCacheUseCase.invoke(room)
        }
    }

    private fun showRoomQuestions(room: Room) {
        viewModelScope.launch {
            val questionsCache = getCachedQuestionsUseCase.invoke(room.id)
            _questionsLiveData.postValue(questionsCache)
        }
    }

    fun onItemSwipedLeft(question: Question, position: Int) {
        deleteQuestion(question)
    }

    private fun saveQuestionToCache(question: Question) {
        viewModelScope.launch {
            try {
                saveQuestionToCacheUseCase.invoke(question, currentRoom)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun deleteQuestion(question: Question) {

    }

}
