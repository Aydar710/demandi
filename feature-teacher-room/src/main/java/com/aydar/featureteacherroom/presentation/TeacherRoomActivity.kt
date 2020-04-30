package com.aydar.featureteacherroom.presentation

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.aydar.demandi.common.base.EXTRA_ROOM
import com.aydar.demandi.common.base.MESSAGE_READ
import com.aydar.demandi.common.base.MESSAGE_RECEIVED_LIKE
import com.aydar.demandi.common.base.bluetooth.ServiceHolder
import com.aydar.demandi.data.model.Like
import com.aydar.demandi.data.model.Question
import com.aydar.demandi.data.model.Room
import com.aydar.featureteacherroom.R
import com.aydar.featureteacherroom.presentation.adapter.QuestionsAdapter
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeRecyclerView
import com.ernestoyaquello.dragdropswiperecyclerview.listener.OnItemSwipeListener
import kotlinx.android.synthetic.main.activity_teacher_room.*
import org.koin.android.viewmodel.ext.android.viewModel

class TeacherRoomActivity : AppCompatActivity() {

    private val teachersViewModel: TeacherRoomViewModel by viewModel()
    private lateinit var adapter: QuestionsAdapter

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_room)

        initToolbar()

        initRecycler()
        initObservers()
        initHandler()
    }

    private fun initToolbar() {
        setSupportActionBar(inc_toolbar as Toolbar)
        val room = intent.getSerializableExtra(EXTRA_ROOM) as Room
        supportActionBar?.title = room.name
        teachersViewModel.room = room
        teachersViewModel.saveSession()
    }

    private fun initObservers() {
        teachersViewModel.questionsLiveData.observe(this, Observer {
            adapter.submitList(it)
        })
    }

    private fun initRecycler() {
        adapter =
            QuestionsAdapter(
                onAnswerClickListener = {
                },
                onLikeClicked = {})
        rv_questions.adapter = adapter
        rv_questions.swipeListener = onItemSwipeListener
        rv_questions.disableSwipeDirection(DragDropSwipeRecyclerView.ListOrientation.DirectionFlag.RIGHT)
    }

    private fun initHandler() {
        ServiceHolder.teacherService.handler = Handler {
            when (it.what) {
                MESSAGE_READ -> {
                    val question = it.obj as Question
                    teachersViewModel.addQuestion(question)
                    true
                }
                MESSAGE_RECEIVED_LIKE -> {
                    val like = it.obj as Like
                    teachersViewModel.handleLike(like)
                    true
                }
                else -> false
            }
        }
    }
}
