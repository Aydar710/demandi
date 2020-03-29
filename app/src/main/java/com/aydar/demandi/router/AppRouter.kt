package com.aydar.demandi.router

import com.aydar.demandi.featurecreateroom.CreateRoomRouter
import com.aydar.demandi.featurejoinroom.JoinRoomRouter
import com.aydar.demandi.featuremain.MainRouter

interface AppRouter : CreateRoomRouter, MainRouter, JoinRoomRouter