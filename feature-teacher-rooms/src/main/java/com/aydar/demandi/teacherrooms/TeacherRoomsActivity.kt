package com.aydar.demandi.teacherrooms

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_teacher_rooms.*
import org.koin.android.viewmodel.ext.android.viewModel

class TeacherRoomsActivity : AppCompatActivity(R.layout.activity_teacher_rooms) {

    private val viewModel: TeacherRoomsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        fab_add.setOnClickListener {
            viewModel.onAddClicked(this)
        }
    }
}
