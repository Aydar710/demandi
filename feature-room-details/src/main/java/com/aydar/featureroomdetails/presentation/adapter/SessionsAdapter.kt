package com.aydar.featureroomdetails.presentation.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aydar.demandi.data.model.Question
import com.aydar.demandi.data.model.Session
import com.aydar.featureroomdetails.R
import kotlinx.android.synthetic.main.item_date.view.*
import kotlinx.android.synthetic.main.item_sessions_question.view.*
import java.text.SimpleDateFormat
import java.util.*

class SessionsAdapter(
    private val onQuestionClickListener: (View, View) -> Unit,
    private val onSaveClickListener: (Session, Question, View, View) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val sessions = mutableListOf<Session>()
    private var rowTypes = mutableListOf<RowType>()
    private lateinit var currentSession: Session

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view: View
        when (viewType) {
            VIEW_TYPE_DATE -> {
                view =
                    LayoutInflater.from(parent.context).inflate(R.layout.item_date, parent, false);
                return DateViewHolder(view)
            }
            VIEW_TYPE_QUESTION -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_sessions_question, parent, false)
                return QuestionViewHolder(view)
            }
            else -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_sessions_question, parent, false)
                return QuestionViewHolder(view)
            }
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DateViewHolder -> {
                holder.bind((rowTypes[position] as SessionRowType).session.date)
            }
            is QuestionViewHolder -> {
                holder.bind((rowTypes[position] as QuestionRowType).question)
            }
        }
    }

    override fun getItemCount(): Int = rowTypes.size

    override fun getItemViewType(position: Int): Int {
        val session = rowTypes[position]
        return when (session) {
            is SessionRowType -> {
                currentSession = session.session
                VIEW_TYPE_DATE
            }
            else -> {
                VIEW_TYPE_QUESTION
            }
        }
    }

    fun submitList(sessions: List<Session>) {
        this.sessions.clear()
        this.sessions.addAll(sessions)
        mapSessionsRowTypeToSessions()
        notifyDataSetChanged()
    }


    private fun mapSessionsRowTypeToSessions() {
        sessions.forEach {
            if (it.questions.isNotEmpty()) {
                val sessionRowType = SessionRowType(it)
                rowTypes.add(sessionRowType)

                it.questions.forEach { question ->
                    val questionRowType = QuestionRowType(question)
                    rowTypes.add(questionRowType)
                }
            }
        }
    }

    inner class DateViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val simpleDateFormat = SimpleDateFormat("dd MMM")

        fun bind(date: Date) {
            with(view) {
                tv_date.text = simpleDateFormat.format(date)
            }
        }
    }

    inner class QuestionViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(question: Question) {
            with(view) {
                tv_question.text = question.text
                et_answer.setText(question.teacherAnswer)

                constraint_question.setOnClickListener {
                    onQuestionClickListener.invoke(constraint_answer, constraint_question)
                }

                tv_save.setOnClickListener {
                    val answer = et_answer.text.toString()
                    question.teacherAnswer = answer
                    tv_save.visibility = View.GONE
                    onSaveClickListener.invoke(
                        currentSession,
                        question,
                        constraint_answer,
                        constraint_question
                    )
                }

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
                                tv_save.visibility = View.VISIBLE
                            } else {
                                tv_save.visibility = View.GONE
                            }
                        }
                    }
                })
            }
        }
    }
}