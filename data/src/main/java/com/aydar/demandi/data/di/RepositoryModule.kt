package com.aydar.demandi.data.di

import com.aydar.demandi.data.repository.QuestionCacheRepository
import com.aydar.demandi.data.repository.RoomCacheRepository
import com.aydar.demandi.data.repository.RoomsRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.koin.dsl.module

val repositoryModule = module {

    single { Firebase.firestore }

    single<RoomsRepository> { RoomsRepository(get()) }

    single<QuestionCacheRepository> { QuestionCacheRepository(get()) }
    single<RoomCacheRepository> { RoomCacheRepository(get()) }
}