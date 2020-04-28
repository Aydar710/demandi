package com.aydar.demandi.teacherrooms.di

import com.aydar.demandi.teacherrooms.domain.ShowRoomsUseCase
import com.aydar.demandi.teacherrooms.presentation.TeacherRoomsViewModel
import org.koin.dsl.module

val teacherRoomsViewModelModule = module {

    factory {
        TeacherRoomsViewModel(get(), get())
    }
}

val teacherRoomsUseCaseModule = module {

    factory { ShowRoomsUseCase(get(), get()) }
}