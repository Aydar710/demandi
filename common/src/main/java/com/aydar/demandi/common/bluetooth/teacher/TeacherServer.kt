package com.aydar.demandi.common.bluetooth.teacher

import com.aydar.demandi.data.model.Room

class TeacherServer(private val teacherMediator: TeacherBluetoothServiceMediator) {

    private var insecureAcceptThread: TeacherAcceptThread? = null
    private lateinit var room: Room

    fun startRoomServer(room: Room) {
        startServer()
        this.room = room
    }

    @Synchronized
    fun startServer() {
        insecureAcceptThread =
            TeacherAcceptThread {
                val connectedThread =
                    TeacherConnectedThread(
                        it,
                        teacherMediator.handler!!,
                        teacherMediator
                    )
                connectedThread.start()
                connectedThread.sendRoomToStudent(room)
                teacherMediator.addStudent(connectedThread)
            }
        try {
            insecureAcceptThread?.start()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}