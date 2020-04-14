package com.aydar.featureroomdetails.di

import com.aydar.featureroomdetails.domain.GetSessionsUseCase
import com.aydar.featureroomdetails.presentation.RoomDetailsViewModel
import org.koin.dsl.module

val roomDetailsViewModelModule = module {

    factory { RoomDetailsViewModel(get(), get()) }
}

val roomDetailsUseCaseModule = module {
    factory { GetSessionsUseCase(get()) }
}