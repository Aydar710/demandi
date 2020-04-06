package com.aydar.demandi.featurecreateroom.di

import com.aydar.demandi.featurecreateroom.domain.CreateRoomUseCase
import com.aydar.demandi.featurecreateroom.presentation.CreateRoomViewModel
import org.koin.dsl.module

val createRoomModule = module {

    factory { CreateRoomUseCase(get()) }

    factory { CreateRoomViewModel(get(), get()) }
}