package com.aydar.demandi.data.di

import com.aydar.demandi.data.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val roomModule = module {
    single { AppDatabase(androidContext()) }
}