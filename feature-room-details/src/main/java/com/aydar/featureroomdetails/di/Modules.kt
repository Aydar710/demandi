package com.aydar.featureroomdetails.di

import com.aydar.featureroomdetails.RoomDetailsViewModel
import org.koin.dsl.module

val roomDetailsViewModelModule = module {

    factory { RoomDetailsViewModel(get()) }
}

val roomDetailsUseCaseModule = module {

}