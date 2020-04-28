package com.aydar.featureteacherroom.di

import com.aydar.featureteacherroom.domain.SaveQuestionToFirestoreUseCase
import com.aydar.featureteacherroom.domain.SaveSessionUseCase
import com.aydar.featureteacherroom.presentation.TeacherRoomViewModel
import org.koin.dsl.module


val teacherRoomModule = module {

    factory { SaveQuestionToFirestoreUseCase(get(), get()) }
    factory { SaveSessionUseCase(get(), get()) }
}

val roomsViewModelModule = module {
    factory {
        TeacherRoomViewModel(
            get(),
            get()
        )
    }
}