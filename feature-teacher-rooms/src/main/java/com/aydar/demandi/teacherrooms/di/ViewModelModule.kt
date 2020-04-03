package com.aydar.demandi.teacherrooms.di

import com.aydar.demandi.teacherrooms.TeacherRoomsViewModel
import org.koin.dsl.module

val viewModelModule = module {

    factory { TeacherRoomsViewModel(get(), get()) }
}