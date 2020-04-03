package com.aydar.demandi.teacherrooms.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.aydar.demandi.teacherrooms.R
import kotlinx.android.synthetic.main.activity_teacher_rooms.*
import org.koin.android.viewmodel.ext.android.viewModel

class TeacherRoomsActivity : AppCompatActivity(R.layout.activity_teacher_rooms) {

    private val viewModel: TeacherRoomsViewModel by viewModel()
    private val adapter = TeacherRoomsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fab_add.setOnClickListener {
            viewModel.onAddClicked(this)
        }

        initRecycler()
        showRooms()
    }

    private fun showRooms() {
        viewModel.roomsLiveData.observe(this, Observer {
            adapter.submitList(it)
        })
        viewModel.showRooms()
    }

    private fun initRecycler() {
        rv_rooms.adapter = adapter
    }
}
