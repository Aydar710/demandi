package com.aydar.demandi

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aydar.demandi.create.CreateRoomActivity
import com.aydar.demandi.join.JoinRoomActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_create.setOnClickListener {
            startActivity(Intent(this, CreateRoomActivity::class.java))
        }

        btn_join.setOnClickListener {
            startActivity(Intent(this, JoinRoomActivity::class.java))
        }
    }
}