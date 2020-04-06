package com.aydar.demandi.data.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.aydar.demandi.data.model.Question

@Dao
interface QuestionDao {

    @Query("SELECT * FROM questions")
    fun getAllQuestions(): List<Question>

    @Insert
    fun saveQuestion(question: Question)

    @Query("DELETE FROM questions ")
    fun clearQuestions()
}