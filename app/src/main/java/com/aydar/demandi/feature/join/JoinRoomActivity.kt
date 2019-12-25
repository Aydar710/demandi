package com.aydar.demandi.feature.join

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import com.aydar.demandi.BL_UUID
import com.aydar.demandi.R
import com.aydar.demandi.base.BaseBluetoothActivity
import com.aydar.demandi.feature.room.student.StudentsRoomActivity
import kotlinx.android.synthetic.main.activity_join_room.*
import java.io.IOException
import java.util.*

class JoinRoomActivity : BaseBluetoothActivity() {

    private lateinit var adapter: JoinAdapter

    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val deviceName = device.name
                    if (deviceName != null) {
                        adapter.addDevice(device)
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_room)

        initRecycler()
        registerFoundReceiver()
        registerBondStateReceiver()
        bluetoothAdapter?.startDiscovery()
    }

    private fun registerFoundReceiver() {
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(receiver, filter)
    }

    private fun registerBondStateReceiver() {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                val action = intent.action
                if (action == BluetoothDevice.ACTION_BOND_STATE_CHANGED) {
                    val device: BluetoothDevice =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)

                    when (device.bondState) {
                        BluetoothDevice.BOND_BONDED -> {
                            ConnectThread(device).start()
                        }
                        BluetoothDevice.BOND_BONDING -> {
                            print("")
                        }
                    }
                }
            }
        }
        val filter = IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        registerReceiver(receiver, filter)
    }

    private fun initRecycler() {
        adapter = JoinAdapter {
            it.createBond()
        }
        rv_rooms.adapter = adapter
    }

    private fun openStudentsRoomActivity() {
        val intent = Intent(this, StudentsRoomActivity::class.java)
        startActivity(intent)
    }

    private inner class ConnectThread(device: BluetoothDevice) : Thread() {

        private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            device.createRfcommSocketToServiceRecord(UUID.fromString(BL_UUID))
        }

        override fun run() {
            // Cancel discovery because it otherwise slows down the connection.
            bluetoothAdapter?.cancelDiscovery()

            mmSocket?.use { socket ->
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                socket.connect()

                // The connection attempt succeeded. Perform work associated with
                // the connection in a separate thread.
                manageMyConnectedSocket(socket)
            }
        }

        // Closes the client socket and causes the thread to finish.
        fun cancel() {
            try {
                mmSocket?.close()
            } catch (e: IOException) {
                Log.e("T", "Could not close the client socket", e)
            }
        }

        private fun manageMyConnectedSocket(socket: BluetoothSocket) {
            runOnUiThread {
                SocketHolder.teachersSocket = socket
                openStudentsRoomActivity()
            }
            cancel()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        unregisterReceiver(receiver)
    }
}
