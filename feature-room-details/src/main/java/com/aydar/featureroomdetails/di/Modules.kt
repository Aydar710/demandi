package com.aydar.featureroomdetails.di

import com.aydar.featureroomdetails.domain.GetSessionsUseCase
import com.aydar.featureroomdetails.domain.SaveQuestionAnswerUseCase
import com.aydar.featureroomdetails.presentation.RoomDetailsViewModel
import org.koin.dsl.module

val roomDetailsViewModelModule = module {

    factory { RoomDetailsViewModel(get(), get(), get(), get()) }
}

val roomDetailsUseCaseModule = module {
    factory { GetSessionsUseCase(get(), get()) }
    factory { SaveQuestionAnswerUseCase(get(), get()) }
}