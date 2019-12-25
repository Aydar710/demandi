package com.aydar.demandi.feature.room.teacher

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.aydar.demandi.R
import com.aydar.demandi.base.BaseViewModelFactory
import com.aydar.demandi.feature.create.TeachersViewModel

class TeachersRoomActivity : AppCompatActivity() {

    private lateinit var teachersViewModel: TeachersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teachers_room)
    }

    private fun initViewModel() {
        teachersViewModel = ViewModelProviders.of(this, BaseViewModelFactory {
            TeachersViewModel()
        })[TeachersViewModel::class.java]
    }
}
