package com.aydar.demandi.featurerooms.di

import com.aydar.demandi.featurerooms.domain.*
import com.aydar.demandi.featurerooms.student.StudentRoomViewModel
import com.aydar.demandi.featurerooms.teacher.TeachersRoomViewModel
import org.koin.dsl.module

val studentsRoomModule = module {

    factory { SaveQuestionToCacheUseCase(get()) }
    factory { SaveRoomToCacheUseCase(get()) }
    factory { GetRoomFromCacheUseCase(get()) }
    factory { GetCachedQuestionsUseCase(get()) }
    factory { SaveQuestionToFirestoreUseCase(get()) }
    factory { SaveSessionUseCase(get()) }

    factory { StudentRoomViewModel(get(), get(), get(), get()) }
}

val roomsViewModelModule = module {
    factory { TeachersRoomViewModel(get(), get()) }
}