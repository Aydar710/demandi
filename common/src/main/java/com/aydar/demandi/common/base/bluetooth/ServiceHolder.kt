package com.aydar.demandi.common.base.bluetooth

class ServiceHolder {

    companion object {
        lateinit var teacherService: TeacherBluetoothService
        lateinit var studentService: StudentBluetoothService
    }
}