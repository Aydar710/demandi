package com.aydar.feature_room_details.di

import com.aydar.feature_room_details.RoomDetailsViewModel
import org.koin.dsl.module

val roomDetailsViewModelModule = module {

    factory { RoomDetailsViewModel(get()) }
}

val roomDetailsUseCaseModule = module {

}