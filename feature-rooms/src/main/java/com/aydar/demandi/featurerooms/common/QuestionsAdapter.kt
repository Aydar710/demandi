package com.aydar.demandi.featurerooms.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aydar.demandi.data.model.Question
import com.aydar.demandi.featurerooms.R
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeAdapter
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_question.view.*

class QuestionsAdapter(dataSet: MutableList<Question> = mutableListOf()) :
    DragDropSwipeAdapter<Question, QuestionsAdapter.QuestionViewHolder>(dataSet) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_question, parent, false)
        return QuestionViewHolder(view)
    }

/*
    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
*/

    override fun getViewHolder(itemView: View): QuestionViewHolder = QuestionViewHolder(itemView)

    override fun getViewToTouchToStartDraggingItem(
        item: Question,
        viewHolder: QuestionViewHolder,
        position: Int
    ): View? {
        return viewHolder.containerView.btn_drag
    }

    override fun onBindViewHolder(
        item: Question,
        viewHolder: QuestionViewHolder,
        position: Int
    ) {
        viewHolder.bind(item)
    }

    fun submitList(questions: List<Question>) {
        dataSet = questions
    }


    class QuestionViewHolder(override val containerView: View) :
        DragDropSwipeAdapter.ViewHolder(containerView),
        LayoutContainer {

        fun bind(question: Question) {
            with(containerView) {
                tv_question.text = question.text
            }
        }
    }
}
