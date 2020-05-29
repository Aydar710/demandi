package com.aydar.demandi.teacherrooms.presentation

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.aydar.demandi.teacherrooms.R
import kotlinx.android.synthetic.main.activity_teacher_rooms.*
import org.koin.android.viewmodel.ext.android.viewModel

class TeacherRoomsActivity : AppCompatActivity(R.layout.activity_teacher_rooms) {

    private val viewModel: TeacherRoomsViewModel by viewModel()
    private lateinit var adapter: TeacherRoomsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fab_add.setOnClickListener {
            viewModel.onAddClicked(this)
        }

        initToolbar()
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
        adapter = TeacherRoomsAdapter() {
            viewModel.onRoomClicked(it, this)
        }
        rv_rooms.adapter = adapter
    }

    private fun initToolbar() {
        val toolbar = inc_toolbar as Toolbar
        toolbar.setBackgroundColor(Color.WHITE)
        toolbar.title = getString(R.string.my_rooms)
        toolbar.setTitleTextColor(Color.BLACK)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }
}
