package com.aydar.demandi.data.di

import com.aydar.demandi.data.repository.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.koin.dsl.module

val repositoryModule = module {

    single { Firebase.firestore }

    single { RoomsRepository(get()) }

    single { QuestionCacheRepository(get()) }
    single { RoomCacheRepository(get()) }
    single { SessionRepository(get()) }
    single { QuestionRepository(get()) }
}