package com.aydar.demandi.di

import com.aydar.demandi.featurecreateroom.CreateRoomRouter
import com.aydar.demandi.featurejoinroom.JoinRoomRouter
import com.aydar.demandi.featuremain.MainRouter
import com.aydar.demandi.router.AppRouter
import com.aydar.demandi.router.AppRouterImpl
import com.aydar.demandi.teacherrooms.TeacherRoomsRouter
import com.aydar.featureauth.AuthRouter
import com.aydar.featureroomdetails.RoomDetailsRouter
import org.koin.dsl.module

val routerModule = module {
    single<AppRouter> { AppRouterImpl() }
    single<CreateRoomRouter> { AppRouterImpl() }
    single<MainRouter> { AppRouterImpl() }
    single<JoinRoomRouter> { AppRouterImpl() }
    single<TeacherRoomsRouter> { AppRouterImpl() }
    single<RoomDetailsRouter> { AppRouterImpl() }
    single<AuthRouter> { AppRouterImpl() }
}