package com.aydar.demandi.featuremain

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val router: MainRouter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btn_create.setOnClickListener {
            router.moveToCreateRoomActivity(this)
        }

        btn_join.setOnClickListener {
            router.moveToJoinRoomActivity(this)
        }
    }
}
