package com.aydar.demandi.featuremain

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val router: MainRouter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btn_teacher.setOnClickListener {
            router.moveToTeacherRoomsActivity(this)
        }

        btn_student.setOnClickListener {
            router.moveToJoinRoomActivity(this)
        }

        initToolbar()
    }

    private fun initToolbar() {
        val toolbar = inc_toolbar as Toolbar
        toolbar.setBackgroundColor(Color.WHITE)
        toolbar.title = getString(R.string.app_name)
        toolbar.setTitleTextColor(Color.BLACK)
        setSupportActionBar(toolbar)
    }
}
