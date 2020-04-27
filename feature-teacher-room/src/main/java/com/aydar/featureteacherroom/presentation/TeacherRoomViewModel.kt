package com.aydar.featureteacherroom.presentation

import android.bluetooth.BluetoothSocket
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aydar.demandi.data.model.Like
import com.aydar.demandi.data.model.Question
import com.aydar.demandi.data.model.Room
import com.aydar.demandi.data.model.Session
import com.aydar.featureteacherroom.domain.SaveQuestionToFirestoreUseCase
import com.aydar.featureteacherroom.domain.SaveSessionUseCase
import kotlinx.coroutines.launch
import java.util.*

class TeacherRoomViewModel(
    private val saveQuestionToFirestoreUseCase: SaveQuestionToFirestoreUseCase,
    private val saveSessionUseCase: SaveSessionUseCase
) :
    ViewModel() {

    lateinit var room: Room

    private val students = mutableListOf<BluetoothSocket>()

    private val _questionsLiveData =
        MutableLiveData<List<Question>>()

    val questionsLiveData: LiveData<List<Question>>
        get() = _questionsLiveData
    private lateinit var session: Session

    init {
        _questionsLiveData.value = listOf()
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
        session =
            Session(date = Date())
        val sessionId = saveSessionUseCase.invoke(session, room.id)
        session.id = sessionId
    }

    fun handleLike(like: Like) {
        val isLikeExists = checkIfLikeExists(like)
        if (isLikeExists) {
            decrementLike(like)
        } else {
            incrementLike(like)
        }
    }

    private fun incrementLike(like: Like) {
        val currentQuestions = _questionsLiveData.value as MutableList
        currentQuestions.forEach {
            if (it.id == like.questionId) {
                it.likes.add(like)
            }
        }
        _questionsLiveData.value = currentQuestions
    }

    private fun decrementLike(like: Like) {
        val currentQuestions = _questionsLiveData.value as MutableList
        currentQuestions.forEach {
            if (it.id == like.questionId) {
                it.likes.remove(like)
            }
        }
        _questionsLiveData.value = currentQuestions
    }

    private fun checkIfLikeExists(like: Like): Boolean {
        _questionsLiveData.value?.forEach { question ->
            question.likes.forEach { questionLike ->
                if (questionLike.questionId == like.questionId && questionLike.userId == like.userId) {
                    return true
                }
            }
        }
        return false
    }
}