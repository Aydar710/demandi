package com.aydar.demandi.featurestudentroom.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aydar.demandi.data.model.Answer
import com.aydar.demandi.data.model.AnswerLike
import com.aydar.demandi.featurestudentroom.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_answer.view.*

class AnswersAdapter(private val onLikeClicked: (Answer) -> Unit, private val userId: String) :
    ListAdapter<Answer, AnswersAdapter.AnswerViewHolder>(AnswerDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_answer, parent, false)
        return AnswerViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun addItem(answer: Answer) {
        val currentList = mutableListOf<Answer>().apply {
            addAll(currentList)
            add(answer)
        }
        submitList(currentList)
    }

    inner class AnswerViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(answer: Answer) {
            with(containerView) {
                tv_student_answer.text = answer.text
                tv_like_count.text = answer.likes.size.toString()

                if (answer.likes.contains(AnswerLike(answer.id, userId))) {
                    iv_light.setImageResource(R.drawable.ic_light_on)
                } else {
                    iv_light.setImageResource(R.drawable.ic_light_off)
                }

                iv_light.setOnClickListener {
                    onLikeClicked.invoke(answer)
                }
            }
        }
    }
}