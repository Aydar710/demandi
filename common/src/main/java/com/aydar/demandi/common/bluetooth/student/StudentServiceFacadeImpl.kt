package com.aydar.demandi.common.bluetooth.student

import android.bluetooth.BluetoothDevice
import android.os.Handler
import com.aydar.demandi.data.model.Message

class StudentServiceFacadeImpl(private val studentService: StudentBluetoothService) :
    StudentServiceFacade {

    override fun startConnecting(device: BluetoothDevice, handler: Handler) {
        studentService.startConnecting(device, handler)
    }

    override fun sendMessage(message: Message) {
        studentService.sendMessage(message)
    }

    override fun sendMessageWithHandlerMsg(message: Message, messageType: Int) {
        studentService.sendMessageWithHandlerMsg(message, messageType)
    }

    override fun setHandler(handler: Handler?) {
        studentService.handler = handler
    }

    override fun closeConnection() {
        studentService.closeConnection()
    }
}