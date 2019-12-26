package com.aydar.demandi.feature.room.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aydar.demandi.R
import com.aydar.demandi.data.Question
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_question.view.*

class QuestionsAdapter :
    ListAdapter<Question, QuestionsAdapter.QuestionViewHolder>(
        QuestionDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_question, parent, false)
        return QuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class QuestionViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bind(question: Question) {
            with(containerView) {
                tv_question.text = question.text
            }
        }
    }
}
