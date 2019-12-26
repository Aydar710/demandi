package com.aydar.demandi.base

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

@SuppressLint("Registered")
open class BaseBluetoothActivity : AppCompatActivity() {

    protected var bluetoothAdapter: BluetoothAdapter? = null

    private lateinit var bluetoothManager: BluetoothManager

    private val BluetoothAdapter.isDisabled: Boolean
        get() = !isEnabled

    //protected lateinit var bluetoothConnectionService: BluetoothConnectionService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        requestTurnOnBluetooth()

        //bluetoothConnectionService = BluetoothConnectionService(this)

    }

    private fun requestTurnOnBluetooth() {
        //TODO: Проверить в onActivityResult подвердил ли пользователь включение
        bluetoothAdapter?.takeIf { it.isDisabled }?.apply {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, 123)
        }
    }
}