package com.aydar.demandi.featurestudentroom.common

import androidx.recyclerview.widget.DiffUtil
import com.aydar.demandi.data.model.Question

class QuestionDiffCallback : DiffUtil.ItemCallback<Question>() {

    override fun areItemsTheSame(oldItem: Question, newItem: Question): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Question, newItem: Question): Boolean =
        oldItem == newItem
}