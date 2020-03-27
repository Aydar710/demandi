package com.aydar.demandi

import android.app.Application
import com.aydar.demandi.common.base.bluetooth.ServiceHolder
import com.aydar.demandi.di.routerModule
import com.aydar.demandi.common.base.bluetooth.StudentBluetoothService
import com.aydar.demandi.common.base.bluetooth.TeacherBluetoothService
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        ServiceHolder.studentService =
            StudentBluetoothService()
        ServiceHolder.teacherService =
            TeacherBluetoothService()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(routerModule)
        }
    }
}