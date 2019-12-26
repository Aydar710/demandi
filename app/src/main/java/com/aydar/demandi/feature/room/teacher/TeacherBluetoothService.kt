package com.aydar.demandi.feature.room.teacher

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.os.Handler
import com.aydar.demandi.MY_UUID_INSECURE
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

// Defines several constants used when transmitting messages between the
// service and the UI.
const val MESSAGE_READ: Int = 0
const val MESSAGE_WRITE: Int = 1
const val MESSAGE_TOAST: Int = 2

class TeacherBluetoothService() {

    lateinit var handler: Handler

    private var mConnectedThread: ConnectedThread? = null

    private var mInsecureAcceptThread: AcceptThread? = null


    constructor(handler: Handler) : this() {
        this.handler = handler
    }

    private inner class AcceptThread : Thread() {

        // The local server socket
        private val mmServerSocket: BluetoothServerSocket?

        private val mBluetoothAdapter: BluetoothAdapter

        init {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

            var tmp: BluetoothServerSocket? = null

            // Create a new listening server socket
            try {
                tmp = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(
                    "Demandi",
                    UUID.fromString(MY_UUID_INSECURE)
                )
            } catch (e: IOException) {
            }

            mmServerSocket = tmp
        }

        override fun run() {

            var socket: BluetoothSocket? = null

            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception

                socket = mmServerSocket!!.accept()

            } catch (e: IOException) {
                e.printStackTrace()
            }

            if (socket != null) {
                manageConnectedSocket(socket)
            }
        }

        private fun manageConnectedSocket(mmSocket: BluetoothSocket) {
            // Start the thread to manage the connection and perform transmissions
            mConnectedThread = ConnectedThread(mmSocket)
            try {
                mConnectedThread!!.start()
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


    inner class ConnectedThread(private val mmSocket: BluetoothSocket) : Thread() {

        private val inStream: InputStream = mmSocket.inputStream
        private val outStream: OutputStream = mmSocket.outputStream
        private val buffer: ByteArray = ByteArray(1024)

        override fun run() {
            while (true) {
                val numBytes = try {
                    inStream.read(buffer)
                } catch (e: IOException) {
                    e.printStackTrace()
                    continue
                }

                val incomingMessage = String(buffer, 0, numBytes)

                val readMsg = handler.obtainMessage(
                    MESSAGE_READ, incomingMessage
                )

                handler.sendMessage(readMsg)
            }
        }

        // Call this method from the activity to shut down the connection.
        fun cancel() {
            try {
                mmSocket.close()
            } catch (e: IOException) {
            }
        }
    }

    @Synchronized
    fun startServer() {
        if (mInsecureAcceptThread == null) {
            mInsecureAcceptThread = AcceptThread()
            try {
                mInsecureAcceptThread?.start()

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                print("")
            }
        }
    }
}
