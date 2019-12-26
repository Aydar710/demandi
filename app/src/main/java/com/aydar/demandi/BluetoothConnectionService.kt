package com.aydar.demandi

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

class BluetoothConnectionService {

    private val mBluetoothAdapter: BluetoothAdapter

    private var mInsecureAcceptThread: AcceptThread? = null

    private var mConnectThread: ConnectThread? = null
    private var mmDevice: BluetoothDevice? = null
    private var deviceUUID: UUID? = null

    private var mConnectedThread: ConnectedThread? = null

    init {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    }

    private inner class AcceptThread : Thread() {

        // The local server socket
        private val mmServerSocket: BluetoothServerSocket?

        init {
            var tmp: BluetoothServerSocket? = null

            // Create a new listening server socket
            try {
                tmp = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(
                    appName,
                    MY_UUID_INSECURE
                )

                Log.d(TAG, "AcceptThread: Setting up Server using: $MY_UUID_INSECURE")
            } catch (e: IOException) {
                Log.e(TAG, "AcceptThread: IOException: " + e.message)
            }

            mmServerSocket = tmp
        }

        override fun run() {
            Log.d(TAG, "run: AcceptThread Running.")

            var socket: BluetoothSocket? = null

            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                Log.d(TAG, "run: RFCOM server socket startServer.....")

                socket = mmServerSocket!!.accept()

                Log.d(TAG, "run: RFCOM server socket accepted connection.")

            } catch (e: IOException) {
                Log.e(TAG, "AcceptThread: IOException: " + e.message)
            }

            if (socket != null) {
                TestSocketHolder.serverSocket = socket
                connected(socket, mmDevice)
            }

            Log.i(TAG, "END mAcceptThread ")
        }

        fun cancel() {
            Log.d(TAG, "cancel: Canceling AcceptThread.")
            try {
                mmServerSocket!!.close()
            } catch (e: IOException) {
                Log.e(TAG, "cancel: Close of AcceptThread ServerSocket failed. " + e.message)
            }
        }
    }

    private inner class ConnectThread(device: BluetoothDevice) : Thread() {
        private lateinit var mmSocket: BluetoothSocket

        init {
            Log.d(TAG, "ConnectThread: started.")
            mmDevice = device
            deviceUUID = MY_UUID_INSECURE
        }

        override fun run() {
            lateinit var tmp: BluetoothSocket
            Log.i(TAG, "RUN mConnectThread ")

            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
                Log.d(
                    TAG,
                    "ConnectThread: Trying to create InsecureRfcommSocket using UUID: $MY_UUID_INSECURE"
                )
                tmp = mmDevice!!.createRfcommSocketToServiceRecord(deviceUUID)
            } catch (e: IOException) {
                Log.e(TAG, "ConnectThread: Could not create InsecureRfcommSocket " + e.message)
            }

            mmSocket = tmp

            // Always cancel discovery because it will slow down a connection
            mBluetoothAdapter.cancelDiscovery()

            // Make a connection to the BluetoothSocket

            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mmSocket.connect()

                Log.d(TAG, "run: ConnectThread connected.")
            } catch (e: IOException) {
                // Close the socket
                try {
                    mmSocket.close()
                    Log.d(TAG, "run: Closed Socket.")
                } catch (e1: IOException) {
                    Log.e(
                        TAG,
                        "mConnectThread: run: Unable to close connection in socket " + e1.message
                    )
                }

                Log.d(TAG, "run: ConnectThread: Could not connect to UUID: $MY_UUID_INSECURE")
            }

            //will talk about this in the 3rd video
            TestSocketHolder.clientSocket = mmSocket
            connected(mmSocket, mmDevice)
        }

        fun cancel() {
            try {
                Log.d(TAG, "cancel: Closing Client Socket.")
                mmSocket.close()
            } catch (e: IOException) {
                Log.e(TAG, "cancel: close() of mmSocket in Connectthread failed. " + e.message)
            }

        }
    }

    @Synchronized
    fun start() {
        Log.d(TAG, "startServer")

        // Cancel any thread attempting to make a connection
        if (mConnectThread != null) {
            mConnectThread!!.cancel()
            mConnectThread = null
        }
        if (mInsecureAcceptThread == null) {
            mInsecureAcceptThread = AcceptThread()
            mInsecureAcceptThread!!.start()
        }
    }

    fun startClient(device: BluetoothDevice) {
        Log.d(TAG, "startClient: Started.")

        mConnectThread = ConnectThread(device)
        mConnectThread!!.start()
    }

    private fun connected(mmSocket: BluetoothSocket, mmDevice: BluetoothDevice?) {
        Log.d(TAG, "connected: Starting.")
        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = ConnectedThread(mmSocket)
        try {
            mConnectedThread!!.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private inner class ConnectedThread(private val mmSocket: BluetoothSocket) : Thread() {
        private val mmInStream: InputStream?
        private val mmOutStream: OutputStream?

        init {
            Log.d(TAG, "ConnectedThread: Starting.")
            var tmpIn: InputStream? = null
            var tmpOut: OutputStream? = null


            try {
                tmpIn = mmSocket.inputStream
                tmpOut = mmSocket.outputStream
            } catch (e: IOException) {
                e.printStackTrace()
            }

            mmInStream = tmpIn
            mmOutStream = tmpOut
        }

        override fun run() {
            val buffer = ByteArray(1024)  // buffer store for the stream

            var bytes: Int // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                // Read from the InputStream
                try {
                    bytes = mmInStream!!.read(buffer)
                    val incomingMessage = String(buffer, 0, bytes)
                    Log.d(TAG, "InputStream: $incomingMessage")
                } catch (e: IOException) {
                    Log.e(TAG, "write: Error reading Input Stream. " + e.message)
                    break
                }

            }
        }

        //Call this from the main activity to send data to the remote device
        fun write(text : String) {
            Log.d(TAG, "write: Writing to outputstream: $text")
            try {
                mmOutStream!!.write(text.toByteArray())
            } catch (e: IOException) {
                Log.e(TAG, "write: Error writing to output stream. " + e.message)
            }
        }


        /* Call this from the main activity to shutdown the connection */
        fun cancel() {
            try {
                mmSocket.close()
            } catch (e: IOException) {
            }

        }
    }

    fun write(text : String){
        mConnectedThread?.write(text)
    }

    companion object {

        private val TAG = "BluetoothConnectionServ"

        private val appName = "MYAPP"

        private val MY_UUID_INSECURE = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66")
    }
}
