package com.aydar.demandi.featurejoinroom

import android.app.ProgressDialog
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.widget.Toolbar
import com.aydar.demandi.common.base.BaseBluetoothActivity
import com.aydar.demandi.common.base.MESSAGE_HIDE_DIALOG
import com.aydar.demandi.common.base.MESSAGE_SHOW_DIALOG
import com.aydar.demandi.common.base.ROOM_NAME_PREFIX
import com.aydar.demandi.common.base.bluetooth.ServiceHolder
import kotlinx.android.synthetic.main.activity_join_room.*
import org.koin.android.ext.android.inject

class JoinRoomActivity : BaseBluetoothActivity() {

    private lateinit var adapter: JoinAdapter

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

        setSupportActionBar(inc_toolbar as Toolbar)
        initRecycler()
        initProgressHandler()
        registerFoundReceiver()
        registerBondStateReceiver()
        bluetoothAdapter!!.startDiscovery()
        showPairedDevices()
    }

    private fun showPairedDevices(){
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
        pairedDevices?.forEach { device ->
            if (device.name.startsWith(ROOM_NAME_PREFIX)) {
                adapter.addDevice(device)
            }
        }
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
        adapter = JoinAdapter {
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

    override fun onDestroy() {
        super.onDestroy()

        unregisterReceiver(receiver)
    }
}
