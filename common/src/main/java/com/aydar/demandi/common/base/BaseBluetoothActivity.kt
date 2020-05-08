package com.aydar.demandi.common.base

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions

@SuppressLint("Registered")
open class BaseBluetoothActivity : AppCompatActivity() {

    protected lateinit var bluetoothAdapter: BluetoothAdapter

    private lateinit var bluetoothManager: BluetoothManager

    private val BluetoothAdapter.isDisabled: Boolean
        get() = !isEnabled

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        runWithPermissions(android.Manifest.permission.ACCESS_COARSE_LOCATION) {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        }

        requestTurnOnBluetooth()
    }

    protected fun requestDiscoverable() {
        val discoverableIntent: Intent =
            Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
                putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 1000)
            }
        startActivityForResult(discoverableIntent, 12345)
    }

    private fun requestTurnOnBluetooth() {
        //TODO: Проверить в onActivityResult потдвердил ли пользователь включение
        bluetoothAdapter.takeIf { it.isDisabled }?.apply {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUST_TURN_ON_BLUETOOTH)
        }
    }

    companion object{
        const val REQUST_TURN_ON_BLUETOOTH = 123
    }
}