package com.aydar.demandi.featuremain

import androidx.appcompat.app.AppCompatActivity

interface MainRouter {

    fun moveToCreateRoomActivity(activity: AppCompatActivity)
    fun moveToJoinRoomActivity(activity: AppCompatActivity)
    fun moveToFoo(activity : AppCompatActivity)
}