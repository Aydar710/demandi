package com.aydar.demandi.common.base.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.Bundle
import android.os.Handler
import com.aydar.demandi.common.base.*
import com.aydar.demandi.data.model.Question
import com.aydar.demandi.data.model.Room
import java.io.*
import java.util.*

class StudentBluetoothService() {

    lateinit var handler: Handler

    lateinit var progressHandler: Handler

    private var mConnectThread: ConnectThread? = null

    private lateinit var mConnectedThread: ConnectedThread

    lateinit var startStudentsRoomActivity: () -> Unit

    fun startConnecting(device: BluetoothDevice) {
        progressHandler.sendEmptyMessage(MESSAGE_SHOW_DIALOG)
        mConnectThread = ConnectThread(device)
        mConnectThread!!.start()
    }

    private inner class ConnectThread(device: BluetoothDevice) : Thread() {

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
            try {
                mConnectedThread = ConnectedThread(mmSocket)
            }catch (e : Exception){
                e.printStackTrace()
            }
            try {
                mConnectedThread.start()
                startStudentsRoomActivity.invoke()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    inner class ConnectedThread(private val mmSocket: BluetoothSocket) : Thread() {

        private val inStream: InputStream = mmSocket.inputStream
        private val outStream: OutputStream = mmSocket.outputStream
        private val buffer: ByteArray = ByteArray(1024)
        private var objOutStream: ObjectOutputStream
        private var objInStream: ObjectInputStream

        init {
            progressHandler.sendEmptyMessage(MESSAGE_HIDE_DIALOG)
            objOutStream = ObjectOutputStream(outStream)
            objInStream = ObjectInputStream(inStream)
        }

        override fun run() {
            while (true) {
                val readObj = objInStream.readObject()
                when (readObj) {
                    is Room -> {
                        manageReadRoom(readObj)
                    }
                }
            }
        }

        // Call this from the activity to send data to the remote device.
        fun writeQuestion(text: String) {
            val bytes: ByteArray = text.toByteArray()
            try {
                val question = Question(text = text)
                objOutStream.writeObject(question)
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

        private fun manageReadRoom(room: Room) {
            val roomMsg = handler.obtainMessage(MESSAGE_GOT_ROOM_INFO, room)
            handler.sendMessage(roomMsg)
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
        mConnectedThread.writeQuestion(text)
    }
}