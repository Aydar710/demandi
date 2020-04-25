package com.aydar.demandi.featurerooms.common

import androidx.recyclerview.widget.DiffUtil
import com.aydar.demandi.data.model.Answer

class AnswerDiffCallback : DiffUtil.ItemCallback<Answer>() {

    override fun areItemsTheSame(oldItem: Answer, newItem: Answer): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: Answer, newItem: Answer): Boolean = oldItem == newItem

}