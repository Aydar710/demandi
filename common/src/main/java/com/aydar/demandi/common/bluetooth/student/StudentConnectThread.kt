package com.aydar.demandi.common.bluetooth.student

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import com.aydar.demandi.common.UUID_INSECURE
import java.io.IOException
import java.util.*

class StudentConnectThread(
    device: BluetoothDevice,
    private val errorWhileConnect: () -> Unit,
    private val socketConnected: (BluetoothSocket) -> Unit
) : Thread() {

    private lateinit var mmSocket: BluetoothSocket
    private var mmDevice: BluetoothDevice = device
    private val mBluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    override fun run() {
        lateinit var tmp: BluetoothSocket

        // Get a BluetoothSocket for a connection with the
        // given BluetoothDevice
        try {
            tmp =
                mmDevice!!.createRfcommSocketToServiceRecord(UUID.fromString(UUID_INSECURE))
        } catch (e: IOException) {
            e.printStackTrace()
        }

        mmSocket = tmp

        // Always cancel discovery because it will slow down a connection
        mBluetoothAdapter.cancelDiscovery()

        // Make a connection to the BluetoothSocket

        try {
            // This is a blocking call and will only return on a
            // successful connection or an exception
            mmSocket.connect()
            manageConnectedSocket(mmSocket)

        } catch (e: IOException) {
            errorWhileConnect.invoke()
            // Close the socket
            try {
                mmSocket.close()
            } catch (e1: IOException) {
                e1.printStackTrace()
            }

        }
    }

    fun cancel() {
        try {
            mmSocket.close()
        } catch (e: IOException) {
        }

    }

    private fun manageConnectedSocket(mmSocket: BluetoothSocket) {
        socketConnected.invoke(mmSocket)
    }
}
