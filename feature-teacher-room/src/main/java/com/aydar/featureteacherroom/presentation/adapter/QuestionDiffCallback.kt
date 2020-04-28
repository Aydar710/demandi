package com.aydar.featureteacherroom.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.aydar.demandi.data.model.Question

class QuestionDiffCallback(
    private val oldList: List<Question>,
    private val newList: List<Question>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldQuestion = oldList[oldItemPosition]
        val newQuestion = newList[newItemPosition]
        return oldQuestion.id == newQuestion.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        //TODO: Переписать. Вопросы сравниваются только по количествам лайков
        val oldQuestion = oldList[oldItemPosition]
        val newQuestion = newList[newItemPosition]
        return oldQuestion.likes.size == newQuestion.likes.size
    }
}