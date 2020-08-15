package com.aydar.demandi.router

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.aydar.demandi.common.EXTRA_ROOM
import com.aydar.demandi.data.model.Room
import com.aydar.demandi.featurecreateroom.EXTRA_ROOM_NAME
import com.aydar.demandi.featurecreateroom.presentation.CreateRoomActivity
import com.aydar.demandi.featuremain.MainActivity
import com.aydar.demandi.featurestudentroom.presentation.StudentRoomActivity
import com.aydar.demandi.teacherrooms.presentation.TeacherRoomsActivity
import com.aydar.featureauth.LoginActivity
import com.aydar.featureroomdetails.presentation.RoomDetailsActivity
import com.aydar.featureteacherroom.presentation.TeacherRoomActivity

class AppRouterImpl : AppRouter {

    override fun moveToTeacherRoomActivity(activity: AppCompatActivity, room: Room) {
        val intent = Intent(activity, TeacherRoomActivity::class.java)
        intent.putExtra(EXTRA_ROOM, room)
        activity.startActivity(intent)
    }

    override fun moveToCreateRoomActivity(activity: AppCompatActivity) {
        activity.startActivity(Intent(activity, CreateRoomActivity::class.java))
    }

    override fun moveToJoinRoomActivity(activity: AppCompatActivity) {
        activity.startActivity(
            Intent(
                activity,
                com.aydar.demandi.featurejoinroom.JoinRoomActivity::class.java
            )
        )
    }

    override fun moveToStudentsRoomActivity(activity: AppCompatActivity) {
        val intent = Intent(activity, StudentRoomActivity::class.java)
        activity.startActivity(intent)
    }

    override fun moveToStudentsRoomActivityWithName(activity: AppCompatActivity, roomName: String) {
        val intent = Intent(activity, StudentRoomActivity::class.java)
        intent.putExtra(EXTRA_ROOM_NAME, roomName)
        activity.startActivity(intent)
    }

    override fun moveToTeacherRoomsActivity(activity: AppCompatActivity) {
        activity.startActivity(Intent(activity, TeacherRoomsActivity::class.java))
    }

    override fun moveToRoomDetailsActivity(room: Room, activity: AppCompatActivity) {
        val intent = Intent(activity, RoomDetailsActivity::class.java)
        intent.putExtra(EXTRA_ROOM, room)
        activity.startActivity(intent)
    }

    override fun moveToTeacherRoomActivity(room: Room, activity: AppCompatActivity) {
        val intent = Intent(activity, TeacherRoomActivity::class.java)
        intent.putExtra(EXTRA_ROOM, room)
        activity.startActivity(intent)
    }

    override fun moveToMainActivity(activity: AppCompatActivity) {
        activity.startActivity(Intent(activity, MainActivity::class.java))
        activity.finish()
    }

    override fun moveToLoginActivity(activity: AppCompatActivity) {
        activity.startActivity(Intent(activity, LoginActivity::class.java))
        activity.finish()
    }
}