package com.aydar.demandi

import android.app.Application
import com.aydar.demandi.base.ServiceHolder
import com.aydar.demandi.feature.room.student.StudentBluetoothService
import com.aydar.demandi.feature.room.teacher.TeacherBluetoothService

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        TestSocketHolder.bluetoothConnectionService = BluetoothConnectionService()
        ServiceHolder.studentService = StudentBluetoothService()
        ServiceHolder.teacherService = TeacherBluetoothService()
    }
}