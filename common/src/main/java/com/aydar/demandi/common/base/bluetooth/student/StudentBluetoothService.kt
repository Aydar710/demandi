package com.aydar.demandi.common.base.bluetooth.student

import android.bluetooth.BluetoothDevice
import android.os.Handler
import com.aydar.demandi.common.base.MESSAGE_CONNECTED_TO_ROOM
import com.aydar.demandi.common.base.MESSAGE_ERROR_WHILE_CONNECT
import com.aydar.demandi.common.base.MESSAGE_HIDE_DIALOG
import com.aydar.demandi.common.base.MESSAGE_SHOW_DIALOG
import com.aydar.demandi.data.model.Message

class StudentBluetoothService {

    private var connectThread: StudentConnectThread? = null
    private var connectedThread: StudentConnectedThread? = null
    var handler: Handler? = null
        set(value) {
            field = value
            connectedThread?.handler = value
        }

    fun startConnecting(device: BluetoothDevice, handler: Handler) {
        this.handler = handler
        handler?.sendEmptyMessage(MESSAGE_SHOW_DIALOG)
        connectThread =
            StudentConnectThread(
                device,
                errorWhileConnect = {
                    handler?.sendEmptyMessage(MESSAGE_ERROR_WHILE_CONNECT)
                },
                socketConnected = {
                    try {
                        connectedThread = StudentConnectedThread(it, handler)
                        connectedThread?.start()
                        //handlerJoinRoom?.sendEmptyMessage(MESSAGE_CONNECTED_TO_ROOM)
                        handler?.sendEmptyMessage(MESSAGE_CONNECTED_TO_ROOM)
                        handler?.sendEmptyMessage(MESSAGE_HIDE_DIALOG)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                })
        connectThread!!.start()
    }

    fun sendMessage(message: Message) {
        connectedThread?.writeMessage(message)
    }

    fun sendMessageWithHandlerMsg(message: Message, messageType: Int) {
        connectedThread?.writeMessageSendingHandlerMsg(message, messageType)
    }

    fun closeConnection(){
        connectedThread?.cancel()
    }
}