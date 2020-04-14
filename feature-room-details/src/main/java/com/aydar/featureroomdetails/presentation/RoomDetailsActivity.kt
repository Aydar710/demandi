package com.aydar.featureroomdetails.presentation

import android.graphics.Color
import android.os.Bundle
import android.view.View
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

        requestDiscoverable()
        initRecycler()
        getSessions()

        initClickListeners()

        initToolbar()
    }

    private fun initClickListeners() {
        fab_door.setOnClickListener {
            bluetoothAdapter?.name = "$ROOM_NAME_PREFIX${room.name}"
            viewModel.openRoom(room, this)
        }
    }

    private fun initRecycler() {
        sessionsAdapter = SessionsAdapter { constraintAnswer, constraintQuestion ->
            if (constraintAnswer.visibility === View.GONE) {
                TransitionManager.beginDelayedTransition(ll_question_answer, AutoTransition())
                constraintAnswer.visibility = View.VISIBLE
                constraintQuestion.background =
                    getDrawable(R.drawable.rounded_rectangle_question_expanded)
            } else {
                TransitionManager.beginDelayedTransition(ll_question_answer, AutoTransition())
                constraintAnswer.visibility = View.GONE
                constraintQuestion.background = getDrawable(R.drawable.rounded_rectangle_question)
            }
        }
        rv_sessions.adapter = sessionsAdapter

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
