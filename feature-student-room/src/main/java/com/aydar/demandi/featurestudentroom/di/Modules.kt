package com.aydar.demandi.featurestudentroom.di

import com.aydar.demandi.featurestudentroom.domain.GetCachedQuestionsUseCase
import com.aydar.demandi.featurestudentroom.domain.GetRoomFromCacheUseCase
import com.aydar.demandi.featurestudentroom.domain.SaveQuestionToCacheUseCase
import com.aydar.demandi.featurestudentroom.domain.SaveRoomToCacheUseCase
import com.aydar.demandi.featurestudentroom.presentation.StudentRoomViewModel
import org.koin.dsl.module

val studentsRoomModule = module {

    factory { SaveQuestionToCacheUseCase(get()) }
    factory { SaveRoomToCacheUseCase(get()) }
    factory { GetRoomFromCacheUseCase(get()) }
    factory { GetCachedQuestionsUseCase(get()) }

    factory {
        StudentRoomViewModel(
            get(),
            get(),
            get(),
            get()
        )
    }
}
