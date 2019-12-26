package com.aydar.demandi.base

import android.bluetooth.BluetoothSocket
import com.aydar.demandi.feature.room.student.StudentBluetoothService
import com.aydar.demandi.feature.room.teacher.TeacherBluetoothService

class ServiceHolder {

    companion object{
        lateinit var teachersSocket : BluetoothSocket
        lateinit var studentSocket : BluetoothSocket

        lateinit var teacherService: TeacherBluetoothService
        lateinit var studentService: StudentBluetoothService
    }
}