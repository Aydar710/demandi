package com.aydar.demandi.featurestudentroom.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aydar.demandi.common.MESSAGE_WRITE
import com.aydar.demandi.common.bluetooth.student.StudentServiceFacade
import com.aydar.demandi.common.bluetoothmessages.MessageSendAnswer
import com.aydar.demandi.common.bluetoothmessages.MessageSendAnswerLike
import com.aydar.demandi.common.bluetoothmessages.MessageSendQuestion
import com.aydar.demandi.common.bluetoothmessages.MessageSendQuestionLike
import com.aydar.demandi.data.model.*
import com.aydar.demandi.featurestudentroom.domain.AnswerLikeCountComparator
import com.aydar.demandi.featurestudentroom.domain.QuestionLikeCountComparator
import com.aydar.demandi.featurestudentroom.domain.usecase.GetCachedQuestionsUseCase
import com.aydar.demandi.featurestudentroom.domain.usecase.GetRoomFromCacheUseCase
import com.aydar.demandi.featurestudentroom.domain.usecase.SaveQuestionToCacheUseCase
import com.aydar.demandi.featurestudentroom.domain.usecase.SaveRoomToCacheUseCase
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import java.util.*

class StudentRoomViewModel(
    private val saveQuestionToCacheUseCase: SaveQuestionToCacheUseCase,
    private val saveRoomToCacheUseCase: SaveRoomToCacheUseCase,
    private val getRoomFromCacheUseCase: GetRoomFromCacheUseCase,
    private val getCachedQuestionsUseCase: GetCachedQuestionsUseCase,
    private val user: FirebaseUser,
    private val studentServiceFacade: StudentServiceFacade
) : ViewModel() {

    lateinit var currentRoom: Room

    private val _questionsLiveData = MutableLiveData<List<Question>>()

    val questionsLiveData: LiveData<List<Question>>
        get() = _questionsLiveData

    init {
        _questionsLiveData.value = listOf()
    }

    fun sendQuestion(question: Question) {
        val msgQuestion = MessageSendQuestion(question)
        studentServiceFacade.sendMessageWithHandlerMsg(msgQuestion, MESSAGE_WRITE)
    }

    fun addReceivedQuestion(question: Question) {
        val questions = _questionsLiveData.value?.toMutableList()
        questions?.add(question)
        _questionsLiveData.value = questions
        saveQuestionToCache(question)
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

    fun handleReceivedRoom(room: Room) {
        currentRoom = Room(room.id, room.name, room.subjectName)
        viewModelScope.launch {
            val roomFromDb = getRoomFromCacheUseCase.invoke(room.id)
            if (roomFromDb == null) {
                saveRoomToCache(room)
            } else {
                //showRoomQuestions(room)
            }
        }
    }

    fun onItemSwipedLeft(question: Question) {
        deleteQuestion(question)
    }

    fun handleReceivedQuestionLike(like: QuestionLike) {
        makeQuestionLikeAction(like)
    }

    fun handleQuestionLike(questionId: String) {
        val like = QuestionLike(questionId, user.uid)
        makeQuestionLikeAction(like)
        val msgSendQuestionLike = MessageSendQuestionLike(like)
        studentServiceFacade.sendMessage(msgSendQuestionLike)
    }

    fun handleReceivedCommandDeleteQuestion(question: Question) {
        deleteQuestion(question)
    }

    fun sendAnswer(answer: Answer) {
        val msgSendAnswer = MessageSendAnswer(answer)
        studentServiceFacade.sendMessage(msgSendAnswer)
    }

    fun handleAnswerLike(answerLike: AnswerLike) {
        makeAnswerLikeAction(answerLike)
        val msgSendAnswerLike = MessageSendAnswerLike(answerLike)
        studentServiceFacade.sendMessage(msgSendAnswerLike)
    }

    fun handleReceivedAnswerLike(answerLike: AnswerLike) {
        makeAnswerLikeAction(answerLike)
    }

    fun closeConnection(){
        studentServiceFacade.closeConnection()
    }

    private fun makeQuestionLikeAction(like: QuestionLike) {
        val isLikeExists = checkIfQuestionLikeExists(like)
        if (isLikeExists) {
            decrementQuestionLike(like)
        } else {
            incrementQuestionLike(like)
        }
    }

    private fun makeAnswerLikeAction(answerLike: AnswerLike) {
        val isLikeExists = checkIfAnswerLikeExists(answerLike)
        if (isLikeExists) {
            decrementAnswerLike(answerLike)
        } else {
            incrementAnswerLike(answerLike)
        }
    }

    private fun incrementAnswerLike(like: AnswerLike) {
        val currentQuestions = _questionsLiveData.value as MutableList
        var questionIndex: Int = 0
        var answerIndex: Int = 0
        currentQuestions.forEach { question ->
            question.studentAnswers.forEach { answer ->
                if (answer.id == like.answerId) {
                    answer.likes.add(like)
                    questionIndex = currentQuestions.indexOf(question)
                    answerIndex = currentQuestions[questionIndex].studentAnswers.indexOf(answer)
                    return@forEach
                }
            }
        }
        Collections.sort(
            currentQuestions[questionIndex].studentAnswers,
            AnswerLikeCountComparator()
        )
        _questionsLiveData.value = currentQuestions
    }

    private fun decrementAnswerLike(like: AnswerLike) {
        val currentQuestions = _questionsLiveData.value as MutableList
        var questionIndex: Int = 0
        var answerIndex: Int = 0
        currentQuestions.forEach { question ->
            question.studentAnswers.forEach { answer ->
                if (answer.id == like.answerId) {
                    answer.likes.remove(like)
                    questionIndex = currentQuestions.indexOf(question)
                    answerIndex = currentQuestions[questionIndex].studentAnswers.indexOf(answer)
                    return@forEach
                }
            }
        }
        Collections.sort(
            currentQuestions[questionIndex].studentAnswers,
            AnswerLikeCountComparator()
        )
        _questionsLiveData.value = currentQuestions
    }

    private fun incrementQuestionLike(like: QuestionLike) {
        val currentQuestions = _questionsLiveData.value as MutableList
        currentQuestions.forEach {
            if (it.id == like.questionId) {
                it.likes.add(like)
            }
        }
        Collections.sort(currentQuestions, QuestionLikeCountComparator())
        _questionsLiveData.value = currentQuestions
    }

    private fun decrementQuestionLike(like: QuestionLike) {
        val currentQuestions = _questionsLiveData.value as MutableList
        currentQuestions.forEach {
            if (it.id == like.questionId) {
                it.likes.remove(like)
            }
        }
        Collections.sort(currentQuestions, QuestionLikeCountComparator())
        _questionsLiveData.value = currentQuestions
    }

    private fun checkIfQuestionLikeExists(like: QuestionLike): Boolean {
        _questionsLiveData.value?.forEach { question ->
            question.likes.forEach { questionLike ->
                if (questionLike.questionId == like.questionId && questionLike.userId == like.userId) {
                    return true
                }
            }
        }
        return false
    }

    private fun checkIfAnswerLikeExists(like: AnswerLike): Boolean {
        _questionsLiveData.value?.forEach { question ->
            question.studentAnswers.forEach { answer ->
                if (answer.likes.contains(like)) {
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
        currentQuestions.removeQuestionById(question)
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

    private fun MutableList<Question>.removeQuestionById(question: Question) {
        this.forEach {
            if (it.id == question.id) {
                this.remove(it)
                return
            }
        }
    }
}
