package com.aydar.demandi.common.base.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.Bundle
import android.os.Handler
import com.aydar.demandi.common.base.*
import com.aydar.demandi.common.base.bluetoothcommands.CommandDeleteQuestion
import com.aydar.demandi.data.model.*
import java.io.*
import java.util.*

class StudentBluetoothService {

    var handler: Handler? = null
    var handlerJoinRoom: Handler? = null
    private var connectThread: ConnectThread? = null
    private lateinit var connectedThread: ConnectedThread
    lateinit var connectedDevice: BluetoothDevice

    fun startConnecting(device: BluetoothDevice) {
        handlerJoinRoom?.sendEmptyMessage(MESSAGE_SHOW_DIALOG)
        handler?.sendEmptyMessage(MESSAGE_SHOW_DIALOG)
        connectThread = ConnectThread(device)
        connectThread!!.start()
    }

    fun sendQuestion(question: Question, hasQuestion: Boolean = false) {
        connectedThread.writeQuestion(question, hasQuestion)
    }

    fun sendQuestionLike(like: QuestionLike, userId: String) {
        connectedThread.writeQuestionLike(like)
    }

    fun sendAnswer(answer: Answer) {
        connectedThread.writeAnswer(answer)
    }

    fun sendAnswerLike(answerLike: AnswerLike) {
        connectedThread.writeAnswerLike(answerLike)
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
                manageConnectedSocket(mmSocket, mmDevice)

            } catch (e: IOException) {
                handlerJoinRoom!!.sendEmptyMessage(MESSAGE_ERROR_WHILE_CONNECT)
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

        private fun manageConnectedSocket(mmSocket: BluetoothSocket, mmDevice: BluetoothDevice?) {
            try {
                if (mmSocket != null) {
                    connectedThread = ConnectedThread(mmSocket)
                    connectedDevice = mmDevice!!
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                connectedThread.start()
                handlerJoinRoom?.sendEmptyMessage(MESSAGE_CONNECTED_TO_ROOM)
                handler?.sendEmptyMessage(MESSAGE_CONNECTED_TO_ROOM)
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
            handlerJoinRoom?.sendEmptyMessage(MESSAGE_HIDE_DIALOG)
            objOutStream = ObjectOutputStream(outStream)
            objInStream = ObjectInputStream(inStream)
        }

        override fun run() {
            while (true) {
                try {
                    readObject()
                } catch (e: IOException) {
                    sendDisconnectedMsg()
                    break
                }
            }
        }

        private fun readObject() {
            val readObj = objInStream.readObject()
            when (readObj) {
                is Room -> {
                    manageReadRoom(readObj)
                }
                is Question -> {
                    manageReadQuestion(readObj)
                }
                is QuestionLike -> {
                    manageReadQuestionLike(readObj)
                }
                is CommandDeleteQuestion -> {
                    manageReadCommandDeleteQuestion(readObj)
                }
                is Answer -> {
                    manageReadAnswer(readObj)
                }
                is AnswerLike -> {
                    manageReadAnswerLike(readObj)
                }
            }
        }

        fun writeQuestion(question: Question, hasQuestion: Boolean = false) {
            try {
                objOutStream.writeObject(question)
            } catch (e: IOException) {

                e.printStackTrace()
                // Send a failure message back to the activity.
                val writeErrorMsg = handler?.obtainMessage(MESSAGE_TOAST)
                val bundle = Bundle().apply {
                    putString("toast", "Couldn't send data to the other device")
                }
                writeErrorMsg?.data = bundle
                handler?.sendMessage(writeErrorMsg)
                return
            }

            if (!hasQuestion) {
                val writtenMsg = handler?.obtainMessage(MESSAGE_WRITE, question)
                handler?.sendMessage(writtenMsg)
            }
        }

        fun writeQuestionLike(like: QuestionLike) {
            try {
                objOutStream.writeObject(like)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun writeAnswerLike(like: AnswerLike) {
            try {
                objOutStream.writeObject(like)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun writeAnswer(answer: Answer) {
            objOutStream.writeObject(answer)
        }

        private fun manageReadRoom(room: Room) {
            val roomMsg = handler?.obtainMessage(MESSAGE_RECEIVED_ROOM_INFO, room)
            handler?.sendMessage(roomMsg)
        }

        private fun manageReadQuestion(question: Question) {
            val questionMsg = handler?.obtainMessage(MESSAGE_RECEIVED_QUESTION, question)
            handler?.sendMessage(questionMsg)
        }

        private fun manageReadQuestionLike(like: QuestionLike) {
            val questionMsg = handler?.obtainMessage(MESSAGE_RECEIVED_QUESTION_LIKE, like)
            handler?.sendMessage(questionMsg)
        }

        private fun manageReadAnswerLike(like: AnswerLike) {
            val likeMsg = handler?.obtainMessage(MESSAGE_RECEIVED_ANSWER_LIKE, like)
            handler?.sendMessage(likeMsg)
        }

        private fun manageReadCommandDeleteQuestion(commandDeleteQuestion: CommandDeleteQuestion) {
            val questionMsg =
                handler?.obtainMessage(MESSAGE_COMMAND_DELETE_QUESTION, commandDeleteQuestion)
            handler?.sendMessage(questionMsg)
        }

        private fun manageReadAnswer(answer: Answer) {
            val answerMsg =
                handler?.obtainMessage(MESSAGE_ANSWER, answer)
            handler?.sendMessage(answerMsg)
        }

        private fun sendDisconnectedMsg() {
            handler?.sendEmptyMessage(MESSAGE_SOCKET_DISCONNECTED)
        }

        // Call this method from the main activity to shut down the connection.
        fun cancel() {
            try {
                mmSocket.close()
            } catch (e: IOException) {
            }
        }
    }
}