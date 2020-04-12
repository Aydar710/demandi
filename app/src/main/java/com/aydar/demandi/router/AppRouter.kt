package com.aydar.demandi.router

import com.aydar.demandi.featurecreateroom.CreateRoomRouter
import com.aydar.demandi.featurejoinroom.JoinRoomRouter
import com.aydar.demandi.featuremain.MainRouter
import com.aydar.demandi.teacherrooms.TeacherRoomsRouter
import com.aydar.featureroomdetails.RoomDetailsRouter

interface AppRouter : CreateRoomRouter, MainRouter, JoinRoomRouter, TeacherRoomsRouter, RoomDetailsRouter