package com.aydar.demandi.common.base.bluetooth

import android.os.Handler
import com.aydar.demandi.data.model.Message
import com.aydar.demandi.data.model.Question
import com.aydar.demandi.data.model.Room

class TeacherBluetoothServiceMediator : Mediator {

    lateinit var handler: Handler

    private var connectedThreads: MutableList<ConnectedThread>? = mutableListOf()

    private var insecureAcceptThread: TeacherAcceptThread? = null

    lateinit var room: Room

    override fun sendMessage(message: Message, sender: ConnectedThread) {
        if (message is Question) {
            if (message.visibleToOthers) {
                broadcastMessage(sender, message)
            }
        } else {
            broadcastMessage(sender, message)
        }
    }

    fun startRoomServer(room: Room) {
        startServer()
        this.room = room
    }

    @Synchronized
    fun startServer() {
        insecureAcceptThread =
            TeacherAcceptThread {
                val connectedThread = ConnectedThread(it, handler, this)
                connectedThread.start()
                connectedThread.sendRoomToStudent(room)
                connectedThreads?.add(connectedThread)
            }
        try {
            insecureAcceptThread?.start()

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

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

    private fun broadcastMessage(
        sender: ConnectedThread,
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
