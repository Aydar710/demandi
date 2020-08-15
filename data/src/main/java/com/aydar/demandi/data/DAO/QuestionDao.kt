package com.aydar.demandi.data.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.aydar.demandi.data.model.QuestionCache

@Dao
interface QuestionDao {

    @Insert
    suspend fun saveQuestion(question: QuestionCache)

    @Query("SELECT * FROM questions WHERE room_id == :roomId")
    suspend fun getQuestions(roomId: String) : List<QuestionCache>

    @Query("DELETE FROM questions ")
    fun clearQuestions()
}