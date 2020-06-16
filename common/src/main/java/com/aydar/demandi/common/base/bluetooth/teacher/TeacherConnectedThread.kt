package com.aydar.demandi.common.base.bluetooth.teacher

import android.bluetooth.BluetoothSocket
import android.os.Handler
import com.aydar.demandi.common.base.bluetoothmessages.MessageDeleteQuestion
import com.aydar.demandi.common.base.bluetoothmessages.MessageSendQuestion
import com.aydar.demandi.common.base.bluetoothmessages.MessageSendQuestionLike
import com.aydar.demandi.common.base.bluetoothmessages.MessageSendRoomInfo
import com.aydar.demandi.common.base.messages.MessageCreator
import com.aydar.demandi.common.base.messages.MessageCreatorImpl
import com.aydar.demandi.data.model.Message
import com.aydar.demandi.data.model.Question
import com.aydar.demandi.data.model.Room
import java.io.*

class TeacherConnectedThread(
    private val mmSocket: BluetoothSocket,
    var handler: Handler,
    private val mediator: Mediator
) : Thread() {

    private val inStream: InputStream = mmSocket.inputStream
    private val outStream: OutputStream = mmSocket.outputStream
    private var objInStream: ObjectInputStream
    private var objOutStream: ObjectOutputStream

    private var messageCreator: MessageCreator

    init {
        objInStream = ObjectInputStream(inStream)
        objOutStream = ObjectOutputStream(outStream)
        messageCreator = MessageCreatorImpl(handler)
    }

    override fun run() {
        while (true) {
            try {
                val readObj = objInStream.readObject()
                val readMsg = readObj as Message
                val shouldSendHandlerMessage =
                    readMsg is MessageSendQuestion || readMsg is MessageSendQuestionLike

                if (shouldSendHandlerMessage) {
                    manageReadObjWithSendingHandlerMsg(readMsg)
                } else {
                    sendMessageToStudents(readMsg)
                }
            } catch (e: Exception) {
                cancel()
            }
        }
    }

    fun sendMessage(message: Message) {
        try {
            objOutStream.writeObject(message)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun sendRoomToStudent(room: Room) {
        sleep(2000)
        val roomMsg = MessageSendRoomInfo(room)
        objOutStream.writeObject(roomMsg)
    }

    fun deleteQuestion(question: Question) {
        val deleteCommand = MessageDeleteQuestion(question)
        objOutStream.writeObject(deleteCommand)
    }

    private fun manageReadObjWithSendingHandlerMsg(message: Message) {
        val msg = messageCreator.createMessage(message)
        handler.sendMessage(msg)
        mediator.sendMessage(message, this)
    }

    private fun sendMessageToStudents(msg: Message) {
        mediator.sendMessage(msg, this)
    }

    // Call this method from the activity to shut down the connection.
    fun cancel() {
        try {
            mmSocket.close()
        } catch (e: IOException) {
        }
    }
}
