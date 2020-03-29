package com.aydar.demandi.featurejoinroom

import androidx.appcompat.app.AppCompatActivity

interface JoinRoomRouter {

    fun moveToStudentsRoomActivity(activity : AppCompatActivity)

    fun moveToStudentsRoomActivityWithName(activity : AppCompatActivity, roomName : String)
}