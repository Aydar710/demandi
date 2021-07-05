package com.aydar.demandi.common.di

import com.aydar.demandi.common.SharedPrefWrapper
import com.aydar.demandi.common.SharedPrefWrapperDeviceName
import com.aydar.demandi.common.bluetooth.student.StudentBluetoothService
import com.aydar.demandi.common.bluetooth.student.StudentServiceFacade
import com.aydar.demandi.common.bluetooth.student.StudentServiceFacadeImpl
import com.aydar.demandi.common.bluetooth.teacher.TeacherBluetoothServiceMediator
import org.koin.dsl.module

val bluetoothServiceModule = module {

    single { StudentBluetoothService() }

    single { TeacherBluetoothServiceMediator() }

    single { SharedPrefWrapper(get()) }
    single { SharedPrefWrapperDeviceName(get()) }

    single<StudentServiceFacade> {
        StudentServiceFacadeImpl(
            get()
        )
    }
}