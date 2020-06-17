package com.aydar.demandi.common.base.di

import com.aydar.demandi.common.base.SharedPrefWrapper
import com.aydar.demandi.common.base.SharedPrefWrapperDeviceName
import com.aydar.demandi.common.base.bluetooth.student.StudentBluetoothService
import com.aydar.demandi.common.base.bluetooth.student.StudentServiceFacade
import com.aydar.demandi.common.base.bluetooth.student.StudentServiceFacadeImpl
import com.aydar.demandi.common.base.bluetooth.teacher.TeacherBluetoothServiceMediator
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