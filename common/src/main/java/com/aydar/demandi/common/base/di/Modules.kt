package com.aydar.demandi.common.base.di

import com.aydar.demandi.common.base.bluetooth.StudentBluetoothService
import com.aydar.demandi.common.base.bluetooth.TeacherBluetoothService
import org.koin.dsl.module

val bluetoothServiceModule = module {

    single { StudentBluetoothService() }

    single { TeacherBluetoothService() }
}