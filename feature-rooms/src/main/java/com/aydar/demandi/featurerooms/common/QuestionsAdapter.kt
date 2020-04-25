package com.aydar.demandi.featurerooms.common

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aydar.demandi.data.model.Answer
import com.aydar.demandi.data.model.Question
import com.aydar.demandi.featurerooms.R
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeAdapter
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_question.view.btn_drag
import kotlinx.android.synthetic.main.item_question.view.tv_question
import kotlinx.android.synthetic.main.item_question_student.view.*

class QuestionsAdapter(
    dataSet: MutableList<Question> = mutableListOf(),
    private val onAnswerClickListener: (Question) -> Unit
) :
    DragDropSwipeAdapter<Question, QuestionsAdapter.QuestionViewHolder>(dataSet) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_question_student, parent, false)
        return QuestionViewHolder(view)
    }

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

    inner class QuestionViewHolder(override val containerView: View) :
        DragDropSwipeAdapter.ViewHolder(containerView),
        LayoutContainer {

        private lateinit var answerAdapter: AnswersAdapter

        fun bind(question: Question) {
            with(containerView) {
                tv_question.text = question.text
                tv_answer.setOnClickListener {
                    val answer = Answer(et_answer.text.toString(), question.id)
                    question.studentAnswers.add(answer)
                    answerAdapter.addItem(answer)
                    onAnswerClickListener.invoke(question)

                    tv_answer.visibility = View.GONE
                }

                answerAdapter = AnswersAdapter()
                rv_answers.adapter = answerAdapter
                answerAdapter.submitList(listOf(Answer("123"), Answer("123"), Answer("123")))
                et_answer.addTextChangedListener(object : TextWatcher {

                    override fun afterTextChanged(s: Editable?) {}

                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        s?.let {
                            if (it.isNotBlank() && it.toString() != question.teacherAnswer) {
                                tv_answer.visibility = View.VISIBLE
                            } else {
                                tv_answer.visibility = View.GONE
                            }
                        }
                    }

                })
            }
        }
    }
}
