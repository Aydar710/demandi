package com.aydar.demandi.featurestudentroom.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aydar.demandi.data.model.Answer
import com.aydar.demandi.data.model.Like
import com.aydar.demandi.data.model.Question
import com.aydar.demandi.featurestudentroom.R
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeAdapter
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_question_student.view.*

class QuestionsAdapter(
    dataSet: MutableList<Question> = mutableListOf(),
    private val userId: String,
    private val onAnswerClickListener: (Answer) -> Unit,
    private val onLikeClicked: (Question) -> Unit,
    private val onQuestionClickListener: (View, View) -> Unit
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
        return viewHolder.containerView.btn_drag2
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

    fun addAnswer(answer: Answer) {
        for (i in dataSet.indices) {
            if (dataSet[i].id == answer.questionId) {
                dataSet[i].studentAnswers.add(answer)
                notifyItemChanged(i)
                return
            }
        }
    }

    inner class QuestionViewHolder(override val containerView: View) :
        DragDropSwipeAdapter.ViewHolder(containerView),
        LayoutContainer {

        private lateinit var answerAdapter: AnswersAdapter

        fun bind(question: Question) {
            with(containerView) {
                tv_question.text = question.text
                tv_answer.setOnClickListener {
                    if (et_answer.text.isNotEmpty()) {
                        val answer = Answer(et_answer.text.toString(), question.id, userId)
                        onAnswerClickListener.invoke(answer)
                        answerAdapter.addItem(answer)
                        question.studentAnswers.add(answer)
                        et_answer.setText("")
                    }
                }
                tv_count.text = question.likes.size.toString()

                if (question.likes.contains(Like(question.id, userId))) {
                    ic_like.setImageResource(R.drawable.ic_rocket_book_filled)
                } else {
                    ic_like.setImageResource(R.drawable.ic_rocket_book)
                }

                ic_like.setOnClickListener {
                    onLikeClicked.invoke(question)
                }

                constraint_question.setOnClickListener {
                    onQuestionClickListener.invoke(constraint_answers, constraint_question)
                }

                setUpAnswersAdapter(this, question)
            }
        }

        private fun setUpAnswersAdapter(containerView: View, question: Question) {
            with(containerView) {
                answerAdapter = AnswersAdapter()
                rv_answers.adapter = answerAdapter
                answerAdapter.submitList(question.studentAnswers.toMutableList())
            }
        }
    }
}
