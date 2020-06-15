package com.aydar.demandi.common.base.bluetooth.teacher

import android.bluetooth.BluetoothDevice
import android.os.Handler
import com.aydar.demandi.data.model.Message

interface StudentServiceFacade {

    fun startConnecting(device: BluetoothDevice, handler : Handler)
    fun sendMessage(message: Message)
    fun sendMessageWithHandlerMsg(message: Message, messageType: Int)
    fun setHandler(handler: Handler?)
}