package com.aydar.demandi.feature.create

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.aydar.demandi.BL_UUID
import com.aydar.demandi.base.BaseBluetoothActivity
import com.aydar.demandi.base.ServiceHolder
import com.aydar.demandi.feature.room.teacher.TeachersRoomActivity
import kotlinx.android.synthetic.main.activity_create_room.*
import java.io.IOException
import java.util.*

class CreateRoomActivity : BaseBluetoothActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.aydar.demandi.R.layout.activity_create_room)

        requestDiscoverable()

        btn_create.setOnClickListener {
            ServiceHolder.teacherService.startServer()
            startTeacherRoomActivity()
        }
    }

    private fun startTeacherRoomActivity() {
        startActivity(Intent(this, TeachersRoomActivity::class.java))
    }

    private fun requestDiscoverable() {
        val discoverableIntent: Intent =
            Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
                putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 1000)
            }
        startActivityForResult(discoverableIntent, 12345)
    }

    private fun openTeachersRoomActivity() {
        val checkingSocket = ServiceHolder.studentSocket
        startActivity(Intent(this, TeachersRoomActivity::class.java))
    }

    private inner class AcceptThread : Thread() {

        private val mmServerSocket: BluetoothServerSocket? by lazy(LazyThreadSafetyMode.NONE) {
            bluetoothAdapter?.listenUsingInsecureRfcommWithServiceRecord(
                "123",
                UUID.fromString(BL_UUID)
            )
        }

        override fun run() {
            // Keep listening until exception occurs or a socket is returned.
            var shouldLoop = true
            while (shouldLoop) {
                val socket: BluetoothSocket? = try {
                    mmServerSocket?.accept()
                } catch (e: IOException) {
                    e.printStackTrace()
                    shouldLoop = false
                    null
                }
                socket?.also {
                    manageMyConnectedSocket(it)
                }
            }
        }

        // Closes the connect socket and causes the thread to finish.
        fun cancel() {
            try {
                mmServerSocket?.close()
            } catch (e: IOException) {
                Log.e("T", "Could not close the connect socket", e)
            }
        }

        private fun manageMyConnectedSocket(bluetoothSocket: BluetoothSocket) {
            runOnUiThread {
                ServiceHolder.studentSocket = bluetoothSocket
                print("")
                //TODO: Don't close the socket
                //cancel()
                openTeachersRoomActivity()
            }
        }
    }
}
