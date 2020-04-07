package com.aydar.demandi.featurerooms.di

import com.aydar.demandi.featurerooms.domain.SaveQuestionToCacheUseCase
import com.aydar.demandi.featurerooms.domain.SaveRoomToCacheUseCase
import com.aydar.demandi.featurerooms.student.StudentRoomViewModel
import org.koin.dsl.module

val studentsRoomModule = module {

    factory { SaveQuestionToCacheUseCase(get()) }
    factory { SaveRoomToCacheUseCase(get()) }

    factory { StudentRoomViewModel(get(), get()) }
}