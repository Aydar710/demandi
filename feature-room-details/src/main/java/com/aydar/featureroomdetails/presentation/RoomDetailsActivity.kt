package com.aydar.featureroomdetails.presentation

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.aydar.demandi.common.base.BaseBluetoothActivity
import com.aydar.demandi.common.base.EXTRA_ROOM
import com.aydar.demandi.common.base.ROOM_NAME_PREFIX
import com.aydar.demandi.data.model.Room
import com.aydar.featureroomdetails.R
import com.aydar.featureroomdetails.presentation.adapter.SessionsAdapter
import kotlinx.android.synthetic.main.activity_room_details.*
import kotlinx.android.synthetic.main.item_sessions_question.*
import org.koin.android.viewmodel.ext.android.viewModel

class RoomDetailsActivity : BaseBluetoothActivity() {

    private val viewModel: RoomDetailsViewModel by viewModel()
    private lateinit var room: Room
    private lateinit var sessionsAdapter: SessionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_details)

        room = intent.getSerializableExtra(EXTRA_ROOM) as Room
        viewModel.currentRoom = room

        initRecycler()
        getSessions()

        initClickListeners()

        initToolbar()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_DISCOVERABLE) {
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(
                    this,
                    getString(R.string.can_not_continue_without_bluetooth),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                bluetoothAdapter?.name = "$ROOM_NAME_PREFIX${room.name}/${room.subjectName}/"
                viewModel.openRoom(room, this)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun initClickListeners() {
        fab_door.setOnClickListener {
            requestDiscoverable()
        }
    }

    private fun initRecycler() {
        sessionsAdapter =
            SessionsAdapter(
                onQuestionClickListener = { constraintAnswer, constraintQuestion ->
                    if (constraintAnswer.visibility === View.GONE) {
                        expandQuestion(constraintAnswer, constraintQuestion)
                    } else {
                        collapseQuestion(constraintAnswer, constraintQuestion)
                    }
                },
                onSaveClickListener = { session, question, constraintAnswer, constraintQuestion ->
                    collapseQuestion(constraintAnswer, constraintQuestion)
                    viewModel.saveQuestionAnswer(room, session, question)
                    Toast.makeText(this, "Ответ сохранен", Toast.LENGTH_SHORT).show()
                }
            )
        rv_sessions.adapter = sessionsAdapter

    }

    private fun collapseQuestion(
        constraintAnswer: View,
        constraintQuestion: View
    ) {
        TransitionManager.beginDelayedTransition(
            ll_question_answer,
            AutoTransition()
        )
        constraintAnswer.visibility = View.GONE
        constraintQuestion.background =
            getDrawable(R.drawable.rounded_rectangle_question)
    }

    private fun expandQuestion(
        constraintAnswer: View,
        constraintQuestion: View
    ) {
        TransitionManager.beginDelayedTransition(
            ll_question_answer,
            AutoTransition()
        )
        constraintAnswer.visibility = View.VISIBLE
        constraintQuestion.background =
            getDrawable(R.drawable.rounded_rectangle_question_expanded)
    }

    private fun getSessions() {
        viewModel.getSessions()
        viewModel.sessionLiveData.observe(this, Observer {
            sessionsAdapter.submitList(it)
        })
    }

    private fun initToolbar() {
        val toolbar = inc_toolbar as Toolbar
        toolbar.setBackgroundColor(Color.WHITE)
        toolbar.title = room.name
        toolbar.setTitleTextColor(Color.BLACK)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }
}
