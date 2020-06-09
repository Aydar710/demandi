package com.aydar.demandi.common.base

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aydar.demandi.data.model.Room
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import org.koin.android.ext.android.inject

@SuppressLint("Registered")
open class BaseBluetoothActivity : AppCompatActivity() {

    protected lateinit var bluetoothAdapter: BluetoothAdapter

    private lateinit var bluetoothManager: BluetoothManager

    private val BluetoothAdapter.isDisabled: Boolean
        get() = !isEnabled

    protected val sharedPrefWrapperDeviceName: SharedPrefWrapperDeviceName by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        runWithPermissions(android.Manifest.permission.ACCESS_COARSE_LOCATION) {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        }

    }

    fun requestTurnOnBluetooth() {
        bluetoothAdapter.takeIf { it.isDisabled }?.apply {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_TURN_ON_BLUETOOTH)
        }
    }

    fun changeDeviceName(room: Room) {
        val newDeviceName = "$ROOM_NAME_PREFIX${room.name}/${room.subjectName}/"
        val oldDeviceName = sharedPrefWrapperDeviceName.getDeviceName()
        if (oldDeviceName == null) {
            sharedPrefWrapperDeviceName.saveDeviceName(bluetoothAdapter.name)
        }
        bluetoothAdapter.name = newDeviceName
    }


    protected fun requestDiscoverable() {
        val discoverableIntent: Intent =
            Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
                putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVERABLE_DURATION_SEC)
            }
        startActivityForResult(discoverableIntent, REQUEST_DISCOVERABLE)
    }

    companion object {
        const val REQUEST_TURN_ON_BLUETOOTH = 123
        const val REQUEST_DISCOVERABLE = 1234
        const val DISCOVERABLE_DURATION_SEC = 1 * 60
        const val DISCOVERABLE_DURATION_MILLIS = DISCOVERABLE_DURATION_SEC * 1000

        const val BLUETOOTH_ADDRESS = "bluetoothAddress"
    }
}