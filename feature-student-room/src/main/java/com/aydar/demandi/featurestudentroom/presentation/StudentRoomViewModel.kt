package com.aydar.demandi.featurestudentroom.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aydar.demandi.common.base.bluetooth.ServiceHolder
import com.aydar.demandi.data.model.Like
import com.aydar.demandi.data.model.Question
import com.aydar.demandi.data.model.Room
import com.aydar.demandi.featurestudentroom.domain.*
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import java.util.*

class StudentRoomViewModel(
    private val saveQuestionToCacheUseCase: SaveQuestionToCacheUseCase,
    private val saveRoomToCacheUseCase: SaveRoomToCacheUseCase,
    private val getRoomFromCacheUseCase: GetRoomFromCacheUseCase,
    private val getCachedQuestionsUseCase: GetCachedQuestionsUseCase,
    private val user: FirebaseUser
) :
    ViewModel() {

    lateinit var currentRoom: Room

    private val _questionsLiveData = MutableLiveData<List<Question>>()

    val questionsLiveData: LiveData<List<Question>>
        get() = _questionsLiveData

    init {
        _questionsLiveData.value = listOf()
    }

    fun sendQuestion(question: Question) {
        ServiceHolder.studentService.sendQuestion(question)
    }

    fun onQuestionReceived(question: Question) {
        val hasQuestion = checkIfHasQuestion(question)
        if (hasQuestion) {
            //TODO update answers
            val currentQuestions = _questionsLiveData.value as MutableList
            for (i in currentQuestions.indices) {
                if (currentQuestions[i].id == question.id) {
                    currentQuestions[i] = question
                }
            }
            _questionsLiveData.value = currentQuestions
        } else {
            addReceivedQuestion(question)
        }
    }

    fun addReceivedQuestion(question: Question) {
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

    fun onItemSwipedLeft(question: Question) {
        deleteQuestion(question)
    }

    fun handleReceivedLike(like: Like) {
        val isLikeExists = checkIfLikeExists(like)
        if (isLikeExists) {
            decrementLike(like)
        } else {
            incrementLike(like)
        }
    }

    fun handleLike(questionId: String) {
        val like = Like(questionId, user.uid)
        val isLikeExists = checkIfLikeExists(like)
        if (isLikeExists) {
            decrementLike(like)
        } else {
            incrementLike(like)
        }
        ServiceHolder.studentService.sendLike(like, user.uid)
    }

    fun handleReceivedCommandDeleteQuestion(question: Question) {
        deleteQuestion(question)
    }

    private fun incrementLike(like: Like) {
        val currentQuestions = _questionsLiveData.value as MutableList
        currentQuestions.forEach {
            if (it.id == like.questionId) {
                it.likes.add(like)
            }
        }
        Collections.sort(currentQuestions, QuestionLikeCountComparator())
        _questionsLiveData.value = currentQuestions
    }

    private fun decrementLike(like: Like) {
        val currentQuestions = _questionsLiveData.value as MutableList
        currentQuestions.forEach {
            if (it.id == like.questionId) {
                it.likes.remove(like)
            }
        }
        Collections.sort(currentQuestions, QuestionLikeCountComparator())
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
        val currentQuestions = _questionsLiveData.value as MutableList
        currentQuestions.remove(question)
        _questionsLiveData.value = currentQuestions
    }

    private fun checkIfHasQuestion(question: Question): Boolean {
        _questionsLiveData.value?.forEach {
            if (it.id == question.id) {
                return true
            }
        }
        return false
    }

}
