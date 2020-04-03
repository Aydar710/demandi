package com.aydar.demandi.featurerooms.di

import com.aydar.demandi.featurerooms.student.StudentRoomViewModel
import org.koin.dsl.module

val studentsRoomModule = module {

    factory { StudentRoomViewModel() }
}