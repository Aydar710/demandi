package com.aydar.demandi.feature.room.teacher

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.aydar.demandi.R
import com.aydar.demandi.base.BaseViewModelFactory
import com.aydar.demandi.base.ServiceHolder
import com.aydar.demandi.data.Question
import com.aydar.demandi.feature.room.common.MESSAGE_READ
import com.aydar.demandi.feature.room.common.QuestionsAdapter
import kotlinx.android.synthetic.main.activity_teachers_room.*

class TeachersRoomActivity : AppCompatActivity() {

    private lateinit var teachersViewModel: TeachersViewModel
    private lateinit var adapter: QuestionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teachers_room)

        initViewModel()
        initRecycler()
        initObservers()
        initHandler()
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
                    val question = Question(it.obj as String)
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
