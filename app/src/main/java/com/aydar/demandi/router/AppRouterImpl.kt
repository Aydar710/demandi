package com.aydar.demandi.router

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.aydar.demandi.feature.join.JoinRoomActivity
import com.aydar.demandi.feature.room.teacher.TeachersRoomActivity
import com.aydar.demandi.featurecreateroom.CreateRoomActivity
import com.aydar.demandi.featurecreateroom.EXTRA_ROOM_NAME

class AppRouterImpl : AppRouter {

    override fun moveToTeacherRoomActivity(activity: AppCompatActivity, roomName: String) {
        val intent = Intent(activity, TeachersRoomActivity::class.java)
        intent.putExtra(EXTRA_ROOM_NAME, roomName)
        activity.startActivity(intent)
    }

    override fun moveToCreateRoomActivity(activity: AppCompatActivity) {
        activity.startActivity(Intent(activity, CreateRoomActivity::class.java))
    }

    override fun moveToJoinRoomActivity(activity: AppCompatActivity) {
        activity.startActivity(Intent(activity, JoinRoomActivity::class.java))
    }
}