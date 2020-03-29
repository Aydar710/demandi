package com.aydar.demandi.featurerooms.common

import androidx.recyclerview.widget.DiffUtil
import com.aydar.demandi.data.model.Question

class QuestionDiffCallback : DiffUtil.ItemCallback<Question>() {

    override fun areItemsTheSame(oldItem: Question, newItem: Question): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Question, newItem: Question): Boolean =
        oldItem == newItem
}