package com.aydar.featureteacherroom.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aydar.demandi.data.model.Question
import com.aydar.featureteacherroom.R
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeAdapter
import com.ernestoyaquello.dragdropswiperecyclerview.listener.OnItemSwipeListener
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_question_student.view.*

class QuestionsAdapter(
    dataSet: MutableList<Question> = mutableListOf(),
    private val onAnswerClickListener: (Question) -> Unit,
    private val onLikeClicked: (Question) -> Unit
) :
    DragDropSwipeAdapter<Question, QuestionsAdapter.QuestionViewHolder>(dataSet) {

    private val onItemSwipeListener = object : OnItemSwipeListener<Question> {
        override fun onItemSwiped(
            position: Int,
            direction: OnItemSwipeListener.SwipeDirection,
            item: Question
        ): Boolean {
            if (direction == OnItemSwipeListener.SwipeDirection.RIGHT_TO_LEFT) {
                return false
            }
            return true
        }
    }

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

    inner class QuestionViewHolder(override val containerView: View) :
        DragDropSwipeAdapter.ViewHolder(containerView),
        LayoutContainer {

        private lateinit var answerAdapter: AnswersAdapter

        fun bind(question: Question) {
            with(containerView) {
                tv_question.text = question.text
                tv_answer.setOnClickListener {
                    /*val answer = Answer(et_answer.text.toString(), question.id)
                    question.studentAnswers.add(answer)
                    answerAdapter.addItem(answer)
                    onAnswerClickListener.invoke(question)

                    tv_answer.visibility = View.GONE*/
                }
                tv_count.text = question.likes.size.toString()

                answerAdapter = AnswersAdapter()
                rv_answers.adapter = answerAdapter
                ic_like.setOnClickListener {
                    onLikeClicked.invoke(question)
                }
            }
        }
    }
}
