package com.aydar.demandi.common.base.bluetooth.teacher

import android.os.Handler
import com.aydar.demandi.data.model.Message
import com.aydar.demandi.data.model.Question
import com.aydar.demandi.data.model.Room

class TeacherBluetoothServiceMediator :
    Mediator {

    private val teacherServer = TeacherServer(this)

    private var connectedThreads: MutableList<TeacherConnectedThread>? = mutableListOf()

    var handler: Handler? = null
        set(value) {
            value?.let { handler ->
                connectedThreads?.forEach {
                    it.handler = handler
                }
                field = handler
            }
        }

    override fun sendMessage(message: Message, sender: TeacherConnectedThread) {
        /*if (message is Question) {
            if (message.visibleToOthers) {
                broadcastMessage(sender, message)
            }
        } else {
            broadcastMessage(sender, message)
        }*/
    }

    fun startServer(room: Room) {
        teacherServer.startRoomServer(room)
    }

    fun deleteQuestion(question: Question) {
        connectedThreads?.forEach {
            try {
                it.deleteQuestion(question)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addStudent(connectedThread: TeacherConnectedThread) {
        connectedThreads?.add(connectedThread)
    }

    private fun broadcastMessage(
        sender: TeacherConnectedThread,
        message: Message
    ) {
        connectedThreads?.forEach {
            try {
                if (it != sender) {
                    it.sendMessage(message)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
