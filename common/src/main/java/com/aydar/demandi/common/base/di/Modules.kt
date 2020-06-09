package com.aydar.demandi.common.base.di

import com.aydar.demandi.common.base.SharedPrefWrapper
import com.aydar.demandi.common.base.SharedPrefWrapperDeviceName
import com.aydar.demandi.common.base.bluetooth.StudentBluetoothService
import com.aydar.demandi.common.base.bluetooth.TeacherBluetoothServiceMediator
import org.koin.dsl.module

val bluetoothServiceModule = module {

    single { StudentBluetoothService() }

    single { TeacherBluetoothServiceMediator() }

    single { SharedPrefWrapper(get()) }
    single { SharedPrefWrapperDeviceName(get()) }
}