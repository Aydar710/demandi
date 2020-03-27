package com.aydar.demandi.common.base.bluetooth

import android.bluetooth.BluetoothSocket
import com.aydar.demandi.common.base.bluetooth.StudentBluetoothService
import com.aydar.demandi.common.base.bluetooth.TeacherBluetoothService

class ServiceHolder {

    companion object {
        lateinit var teachersSocket: BluetoothSocket
        lateinit var studentSocket: BluetoothSocket

        lateinit var teacherService: TeacherBluetoothService
        lateinit var studentService: StudentBluetoothService
    }
}