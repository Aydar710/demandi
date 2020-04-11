package com.aydar.demandi.featurerooms.teacher

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.aydar.demandi.common.base.BaseViewModelFactory
import com.aydar.demandi.common.base.EXTRA_ROOM
import com.aydar.demandi.common.base.MESSAGE_READ
import com.aydar.demandi.common.base.bluetooth.ServiceHolder
import com.aydar.demandi.data.model.Question
import com.aydar.demandi.data.model.Room
import com.aydar.demandi.featurerooms.R
import com.aydar.demandi.featurerooms.common.QuestionsAdapter
import kotlinx.android.synthetic.main.activity_teachers_room.*

class TeachersRoomActivity : AppCompatActivity() {

    private lateinit var teachersViewModel: TeachersViewModel
    private lateinit var adapter: QuestionsAdapter
    private lateinit var room: Room

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teachers_room)

        initToolbar()

        initViewModel()
        initRecycler()
        initObservers()
        initHandler()
    }

    private fun initToolbar() {
        setSupportActionBar(inc_toolbar as Toolbar)
        val room = intent.getSerializableExtra(EXTRA_ROOM) as Room
        supportActionBar?.title = room.name
        this.room = room
    }

    private fun initObservers() {
        teachersViewModel.questionsLiveData.observe(this, Observer {
            adapter.submitList(it)
        })
    }

    private fun initRecycler() {
        adapter = QuestionsAdapter()
        rv_questions.adapter = adapter
    }

    private fun initHandler() {
        ServiceHolder.teacherService.handler = Handler {
            when (it.what) {
                MESSAGE_READ -> {
                    val question =
                        Question(text = it.obj as String)
                    teachersViewModel.addQuestion(question)
                    true
                }
                else -> false
            }
        }
    }

    private fun initViewModel() {
        teachersViewModel = ViewModelProviders.of(this, BaseViewModelFactory {
            TeachersViewModel()
        })[TeachersViewModel::class.java]

    }
}
