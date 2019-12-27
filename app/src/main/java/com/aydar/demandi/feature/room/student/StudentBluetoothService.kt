package com.aydar.demandi.feature.room.student

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.Bundle
import android.os.Handler
import com.aydar.demandi.UUID_INSECURE
import com.aydar.demandi.feature.room.common.MESSAGE_HIDE_DIALOG
import com.aydar.demandi.feature.room.common.MESSAGE_SHOW_DIALOG
import com.aydar.demandi.feature.room.common.MESSAGE_TOAST
import com.aydar.demandi.feature.room.common.MESSAGE_WRITE
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

class StudentBluetoothService() {

    lateinit var handler: Handler

    lateinit var progressHandler : Handler

    private var mConnectThread: ConnectThread? = null

    private lateinit var mConnectedThread: ConnectedThread

    lateinit var startStudentsRoomActivity: () -> Unit

    constructor(handler: Handler) : this() {
        this.handler = handler
    }

    fun startConnecting(device: BluetoothDevice) {
        progressHandler.sendEmptyMessage(MESSAGE_SHOW_DIALOG)
        mConnectThread = ConnectThread(device)
        mConnectThread!!.start()
    }

    private inner class ConnectThread(device: BluetoothDevice) : Thread() {

        private lateinit var mmSocket: BluetoothSocket
        private lateinit var mmDevice: BluetoothDevice
        private val mBluetoothAdapter: BluetoothAdapter

        init {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            mmDevice = device
        }

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

            } catch (e: IOException) {
                // Close the socket
                try {
                    mmSocket.close()
                } catch (e1: IOException) {
                    e1.printStackTrace()
                }

            }

            manageConnectedSocket(mmSocket, mmDevice)
        }

        fun cancel() {
            try {
                mmSocket.close()
            } catch (e: IOException) {
            }

        }

        private fun manageConnectedSocket(mmSocket: BluetoothSocket, mmDevice: BluetoothDevice?) {
            mConnectedThread = ConnectedThread(mmSocket)
            try {
                mConnectedThread.start()
                startStudentsRoomActivity.invoke()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    inner class ConnectedThread(private val mmSocket: BluetoothSocket) : Thread() {

        private val mmInStream: InputStream = mmSocket.inputStream
        private val mmOutStream: OutputStream = mmSocket.outputStream
        private val mmBuffer: ByteArray = ByteArray(1024)

        init {
            progressHandler.sendEmptyMessage(MESSAGE_HIDE_DIALOG)
        }

        override fun run() {

        }

        // Call this from the main activity to send data to the remote device.
        fun write(text: String) {
            val bytes: ByteArray = text.toByteArray()
            try {
                mmOutStream.write(bytes)
            } catch (e: IOException) {

                e.printStackTrace()
                // Send a failure message back to the activity.
                val writeErrorMsg = handler.obtainMessage(MESSAGE_TOAST)
                val bundle = Bundle().apply {
                    putString("toast", "Couldn't send data to the other device")
                }
                writeErrorMsg.data = bundle
                handler.sendMessage(writeErrorMsg)
                return
            }

            val writtenMsg = handler.obtainMessage(MESSAGE_WRITE, text)

            handler.sendMessage(writtenMsg)
        }

        // Call this method from the main activity to shut down the connection.
        fun cancel() {
            try {
                mmSocket.close()
            } catch (e: IOException) {
            }
        }
    }

    fun sendQuestion(text: String) {
        mConnectedThread.write(text)
    }
}