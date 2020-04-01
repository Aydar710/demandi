package com.aydar.demandi.featurejoinroom

import android.app.ProgressDialog
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.widget.Toolbar
import com.aydar.demandi.common.base.*
import com.aydar.demandi.common.base.bluetooth.ServiceHolder
import kotlinx.android.synthetic.main.activity_join_room.*
import org.koin.android.ext.android.inject
import java.io.IOException
import java.util.*

class JoinRoomActivity : BaseBluetoothActivity() {

    private lateinit var adapter: com.aydar.demandi.featurejoinroom.JoinAdapter

    private lateinit var progressDialog: ProgressDialog

    private val router: JoinRoomRouter by inject()

    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val deviceName = device?.name
                    if (deviceName != null) {
                        if (deviceName.startsWith(ROOM_NAME_PREFIX)) {
                            adapter.addDevice(device)
                        }
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_room)

        router.moveToStudentsRoomActivity(this)
        finish()
        setSupportActionBar(inc_toolbar as Toolbar)
        initRecycler()
        initProgressHandler()
        registerFoundReceiver()
        registerBondStateReceiver()
        bluetoothAdapter!!.startDiscovery()
    }

    private fun initProgressHandler() {
        ServiceHolder.studentService.progressHandler = Handler {
            when (it.what) {
                MESSAGE_SHOW_DIALOG -> {
                    showProgress();
                    true
                }
                MESSAGE_HIDE_DIALOG -> {
                    hideProgress()
                    true
                }
                else -> false
            }
        }
    }

    private fun showProgress() {
        progressDialog = ProgressDialog.show(
            this, "Соединение"
            , "Пожалуйста, подождите...", true
        )
    }

    private fun hideProgress() {
        progressDialog.dismiss()
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
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)

                    when (device?.bondState) {
                        BluetoothDevice.BOND_BONDED -> {
                            connectToDevice(device)
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

    private fun connectToDevice(device: BluetoothDevice) {
        ServiceHolder.studentService.startStudentsRoomActivity = {
            startStudentsRoomActivity(device.name.drop(ROOM_NAME_PREFIX.length))
        }
        ServiceHolder.studentService.startConnecting(device)
    }

    private fun startStudentsRoomActivity(roomName: String) {
        router.moveToStudentsRoomActivityWithName(this, roomName)
    }

    private fun initRecycler() {
        adapter = com.aydar.demandi.featurejoinroom.JoinAdapter {
            if (it.bondState == BluetoothDevice.BOND_BONDED) {
                connectToDevice(it)
            } else {
                it.createBond()
            }
        }
        rv_rooms.adapter = adapter
    }

    private fun openStudentsRoomActivity() {
        router.moveToStudentsRoomActivity(this)
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
                openStudentsRoomActivity()
            }
            //TODO: Don't close the socket
            //cancel()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        unregisterReceiver(receiver)
    }
}
