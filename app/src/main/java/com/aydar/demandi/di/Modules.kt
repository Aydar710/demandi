package com.aydar.demandi.di

import com.aydar.demandi.router.AppRouter
import com.aydar.demandi.router.AppRouterImpl
import com.aydar.demandi.featurecreateroom.CreateRoomRouter
import org.koin.dsl.module

val routerModule = module {
    single<AppRouter> { AppRouterImpl() }
    single<CreateRoomRouter> { AppRouterImpl() }
}