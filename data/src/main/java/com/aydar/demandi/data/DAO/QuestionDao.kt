package com.aydar.demandi.data.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.aydar.demandi.data.model.Question
import com.aydar.demandi.data.model.QuestionCache

@Dao
interface QuestionDao {

    @Query("SELECT * FROM questions")
    fun getAllQuestions(): List<Question>

    @Insert
    fun saveQuestion(question: QuestionCache)

    @Query("DELETE FROM questions ")
    fun clearQuestions()
}