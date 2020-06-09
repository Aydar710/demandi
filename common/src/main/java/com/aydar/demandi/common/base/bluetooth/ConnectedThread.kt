package com.aydar.demandi.common.base.bluetooth

import android.bluetooth.BluetoothSocket
import android.os.Handler
import com.aydar.demandi.common.base.bluetoothcommands.CommandDeleteQuestion
import com.aydar.demandi.common.base.messages.MessageCreator
import com.aydar.demandi.common.base.messages.MessageCreatorImpl
import com.aydar.demandi.data.model.Answer
import com.aydar.demandi.data.model.Message
import com.aydar.demandi.data.model.Question
import com.aydar.demandi.data.model.Room
import java.io.*

class ConnectedThread(
    private val mmSocket: BluetoothSocket,
    private val handler: Handler,
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
                if (readObj is Answer) {
                    manageReadObj(readObj)
                } else {
                    manageReadObjWithSendingHandlerMsg(readObj)
                }
                /*when (readObj) {
                    is Question -> {
                        manageReadQuestion(readObj)
                    }
                    is Answer -> {
                        manageReadAnswer(readObj)
                    }
                    is QuestionLike -> {
                        manageReadQuestionLike(readObj)
                    }
                    is AnswerLike -> {
                        manageReadAnswerLike(readObj)
                    }
                }*/

            } catch (e: Exception) {
                cancel()
            }
        }
    }

    fun sendMessage(message: Message) {
        objOutStream.writeObject(message)
    }

    fun sendRoomToStudent(room: Room) {
        sleep(2000)
        objOutStream.writeObject(room)
    }

    fun deleteQuestion(question: Question) {
        val deleteCommand = CommandDeleteQuestion(question)
        objOutStream.writeObject(deleteCommand)
    }

    private fun manageReadObjWithSendingHandlerMsg(obj: Any) {
        val msg = messageCreator.createMessage(obj as Message)
        handler.sendMessage(msg)
        mediator.sendMessage(obj, this)
    }

    private fun manageReadObj(obj: Any) {
        mediator.sendMessage(obj as Message, this)
    }

    // Call this method from the activity to shut down the connection.
    fun cancel() {
        try {
            mmSocket.close()
        } catch (e: IOException) {
        }
    }
}
