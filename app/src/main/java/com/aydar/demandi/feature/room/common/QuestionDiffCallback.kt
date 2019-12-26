package com.aydar.demandi.feature.room.common

import androidx.recyclerview.widget.DiffUtil
import com.aydar.demandi.data.Question

class QuestionDiffCallback : DiffUtil.ItemCallback<Question>() {

    override fun areItemsTheSame(oldItem: Question, newItem: Question): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Question, newItem: Question): Boolean =
        oldItem == newItem
}