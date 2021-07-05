package com.aydar.demandi.common.bluetooth.student

import android.bluetooth.BluetoothSocket
import android.os.Bundle
import android.os.Handler
import com.aydar.demandi.common.MESSAGE_SOCKET_DISCONNECTED
import com.aydar.demandi.common.MESSAGE_TOAST
import com.aydar.demandi.common.messages.MessageCreator
import com.aydar.demandi.common.messages.MessageCreatorImpl
import com.aydar.demandi.data.model.Message
import java.io.*

class StudentConnectedThread(private val mmSocket: BluetoothSocket, var handler: Handler?) :
    Thread() {

    private val inStream: InputStream = mmSocket.inputStream
    private val outStream: OutputStream = mmSocket.outputStream
    private var objOutStream: ObjectOutputStream
    private var objInStream: ObjectInputStream

    private val messageCreator: MessageCreator

    init {
        objOutStream = ObjectOutputStream(outStream)
        objInStream = ObjectInputStream(inStream)
        messageCreator = MessageCreatorImpl(handler!!)
    }


    override fun run() {
        while (true) {
            try {
                readObject()
            } catch (e: IOException) {
                sendDisconnectedMsg()
                break
            }
        }
    }

    fun writeMessage(message: Message) {
        try {
            objOutStream.writeObject(message)
        } catch (e: Exception) {
            handleErrorWhenSendingMessage(e)
            return
        }
    }

    fun writeMessageSendingHandlerMsg(message: Message, messageType: Int) {
        //message.messageType = messageType
        try {
            objOutStream.writeObject(message)
        } catch (e: Exception) {
            handleErrorWhenSendingMessage(e)
        }

        val msg = handler?.obtainMessage(messageType, message)
        handler?.sendMessage(msg)
    }

    private fun readObject() {
        val readObj = objInStream.readObject()
        manageReadMessage(readObj as Message)
    }

    private fun handleErrorWhenSendingMessage(e: Exception) {
        e.printStackTrace()
        // Send a failure message back to the activity.
        val writeErrorMsg = handler?.obtainMessage(MESSAGE_TOAST)
        val bundle = Bundle().apply {
            putString("toast", "Couldn't send data to the other device")
        }
        writeErrorMsg?.data = bundle
        handler?.sendMessage(writeErrorMsg)
    }

    private fun sendDisconnectedMsg() {
        handler?.sendEmptyMessage(MESSAGE_SOCKET_DISCONNECTED)
    }

    private fun manageReadMessage(message: Message) {
        val msg = messageCreator.createMessage(message)
        handler?.sendMessage(msg)
    }

    // Call this method from the main activity to shut down the connection.
    fun cancel() {
        try {
            mmSocket.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
