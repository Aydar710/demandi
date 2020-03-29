package com.aydar.demandi.di

import com.aydar.demandi.featurecreateroom.CreateRoomRouter
import com.aydar.demandi.featurejoinroom.JoinRoomRouter
import com.aydar.demandi.featuremain.MainRouter
import com.aydar.demandi.router.AppRouter
import com.aydar.demandi.router.AppRouterImpl
import org.koin.dsl.module

val routerModule = module {
    single<AppRouter> { AppRouterImpl() }
    single<CreateRoomRouter> { AppRouterImpl() }
    single<MainRouter> { AppRouterImpl() }
    single<JoinRoomRouter> { AppRouterImpl() }
}