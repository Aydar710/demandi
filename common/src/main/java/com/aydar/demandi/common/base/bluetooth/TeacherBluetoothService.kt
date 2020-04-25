package com.aydar.demandi.common.base.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.os.Handler
import com.aydar.demandi.common.base.MESSAGE_READ
import com.aydar.demandi.common.base.MESSAGE_RECEIVED_ANSWER
import com.aydar.demandi.common.base.UUID_INSECURE
import com.aydar.demandi.data.model.Answer
import com.aydar.demandi.data.model.Question
import com.aydar.demandi.data.model.Room
import java.io.*
import java.util.*

class TeacherBluetoothService() {

    lateinit var handler: Handler

    private var connectedThreads: MutableList<ConnectedThread>? = mutableListOf()

    private var insecureAcceptThread: AcceptThread? = null

    lateinit var room: Room

    private inner class AcceptThread : Thread() {

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
            val connectedThread = ConnectedThread(mmSocket)
            try {
                connectedThread!!.start()
                connectedThread!!.sendRoomToStudent(room)
                connectedThreads?.add(connectedThread)
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
        private var objInStream: ObjectInputStream
        private var objOutStream: ObjectOutputStream

        init {
            objInStream = ObjectInputStream(inStream)
            objOutStream = ObjectOutputStream(outStream)
        }

        override fun run() {
            while (true) {
                try {
                    val readObj = objInStream.readObject()
                    when (readObj) {
                        is Question -> {
                            manageReadQuestion(readObj)
                        }
                        is Answer -> {
                            manageReadAnswer(readObj)
                        }
                    }

                } catch (e: Exception) {
                    cancel()
                }
            }
        }

        fun sendRoomToStudent(room: Room) {
            sleep(2000)
            objOutStream.writeObject(room)
        }

        private fun sendQuestion(question: Question) {
            objOutStream.writeObject(question)
        }

        private fun sendAnswer(answer: Answer){
            objOutStream.writeObject(answer)
        }

        private fun manageReadQuestion(question: Question) {
            val readMsg = handler.obtainMessage(
                MESSAGE_READ, question.text
            )

            connectedThreads?.forEach {
                try {
                    if (it != this) {
                        it.sendQuestion(question)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            handler.sendMessage(readMsg)
        }

        private fun manageReadAnswer(answer: Answer) {
            val questionMsg = handler.obtainMessage(MESSAGE_RECEIVED_ANSWER, answer)
            handler.sendMessage(questionMsg)

            connectedThreads?.forEach {
                try {
                    if (it != this) {
                        it.sendAnswer(answer)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
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
        if (insecureAcceptThread == null) {
            insecureAcceptThread = AcceptThread()
            try {
                insecureAcceptThread?.start()

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                print("")
            }
        }
    }
}
