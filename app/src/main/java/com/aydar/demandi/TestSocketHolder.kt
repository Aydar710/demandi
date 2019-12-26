package com.aydar.demandi

import android.annotation.SuppressLint
import android.bluetooth.BluetoothSocket

class TestSocketHolder {

    companion object{
        lateinit var serverSocket: BluetoothSocket
        lateinit var clientSocket: BluetoothSocket

        @SuppressLint("StaticFieldLeak")
        lateinit var bluetoothConnectionService: BluetoothConnectionService
    }


}
