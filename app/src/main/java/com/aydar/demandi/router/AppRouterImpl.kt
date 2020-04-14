package com.aydar.demandi.router

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.aydar.demandi.common.base.EXTRA_ROOM
import com.aydar.demandi.data.model.Room
import com.aydar.demandi.featurecreateroom.EXTRA_ROOM_NAME
import com.aydar.demandi.featurecreateroom.presentation.CreateRoomActivity
import com.aydar.demandi.featurerooms.student.StudentRoomActivity
import com.aydar.demandi.featurerooms.teacher.TeachersRoomActivity
import com.aydar.demandi.teacherrooms.presentation.TeacherRoomsActivity
import com.aydar.featureroomdetails.presentation.RoomDetailsActivity

class AppRouterImpl : AppRouter {

    override fun moveToTeacherRoomActivity(activity: AppCompatActivity, room: Room) {
        val intent = Intent(activity, TeachersRoomActivity::class.java)
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

    override fun moveToTeachersRoomActivity(room: Room, activity: AppCompatActivity) {
        val intent = Intent(activity, TeachersRoomActivity::class.java)
        intent.putExtra(EXTRA_ROOM, room)
        activity.startActivity(intent)
    }

    override fun moveToFoo(activity: AppCompatActivity) {
        activity.startActivity(Intent(activity, TeacherRoomsActivity::class.java))
    }
}