package com.aydar.featureteacherroom.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.aydar.demandi.data.model.Answer

class AnswerDiffCallback : DiffUtil.ItemCallback<Answer>() {

    override fun areItemsTheSame(oldItem: Answer, newItem: Answer): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Answer, newItem: Answer): Boolean = oldItem == newItem

}