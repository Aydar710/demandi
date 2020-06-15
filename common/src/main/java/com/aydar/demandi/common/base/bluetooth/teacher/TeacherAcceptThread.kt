package com.aydar.demandi.common.base.bluetooth.teacher

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import com.aydar.demandi.common.base.UUID_INSECURE
import java.io.IOException
import java.util.*

class TeacherAcceptThread(private val connectionEstablished: (BluetoothSocket) -> Unit) :
    Thread() {

    // The local server socket
    private val mmServerSocket: BluetoothServerSocket?

    private val mBluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    init {

        var tmp: BluetoothServerSocket? = null

        // Create a new listening server socket
        try {
            tmp = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(
                "Demandi",
                UUID.fromString(UUID_INSECURE)
            )
        } catch (e: IOException) {
        }

        mmServerSocket = tmp
    }

    override fun run() {

        var socket: BluetoothSocket? = null

        while (true) {
            try {
                socket = mmServerSocket?.accept()

            } catch (e: IOException) {
                e.printStackTrace()
            }

            if (socket != null) {
                manageConnectedSocket(socket)
            }
        }
    }

    private fun manageConnectedSocket(mmSocket: BluetoothSocket) {
        // Start the thread to manage the connection and perform transmissions
        try {
            connectionEstablished.invoke(mmSocket)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun cancel() {
        try {
            mmServerSocket!!.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}