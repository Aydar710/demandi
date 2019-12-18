package com.aydar.demandi

import android.app.Activity
import android.bluetooth.*
import android.bluetooth.le.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.ParcelUuid
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*




class CreateRoomActivity : AppCompatActivity() {

    private lateinit var bluetoothAdapter: BluetoothAdapter

    private val BluetoothAdapter.isDisabled: Boolean
        get() = !isEnabled

    private lateinit var mScanResults: HashMap<String, BluetoothDevice>

    private var mScanning = false

    private var mHandler = Handler()

    private lateinit var mScanCallback: BtleScanCallback

    private lateinit var bluetoothLeScanner: BluetoothLeScanner

    private lateinit var mBluetoothLeAdvertiser: BluetoothLeAdvertiser

    private lateinit var mGattServer: BluetoothGattServer

    private val SERVICE_UUID = UUID.fromString("service uuid")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_room)

        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        //val gattServerCallback = GattServerCallback()
        //mGattServer = bluetoothManager.openGattServer(this, gattServerCallback)
        //setupServer()
        //startAdvertising()

        packageManager.takeIf { it.missingSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE) }
            ?.also {
                Toast.makeText(this, "BLE Не поддерживается", Toast.LENGTH_SHORT).show()
                finish()
            }

        bluetoothAdapter.takeIf { it.isDisabled }?.apply {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, 123)
        }

        if (bluetoothAdapter.isEnabled) {
            bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
            //startScan()
        }

        val advertiser = BluetoothAdapter.getDefaultAdapter().bluetoothLeAdvertiser

        if (!bluetoothAdapter.isMultipleAdvertisementSupported) {
            Toast.makeText(this, "MultiAdvertising is not supported", Toast.LENGTH_SHORT).show()
            return
        }
        mBluetoothLeAdvertiser = bluetoothAdapter.bluetoothLeAdvertiser;
    }

    private inner class GattServerCallback : BluetoothGattServerCallback()

    private fun setupServer(){
        val service = BluetoothGattService(
            SERVICE_UUID,
            BluetoothGattService.SERVICE_TYPE_PRIMARY
        )
        mGattServer.addService(service)
    }

    private fun startAdvertising(){
        val settings = AdvertiseSettings.Builder()
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
            .setConnectable(true)
            .setTimeout(0)
            .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_LOW)
            .build()

        val parcelUuid = ParcelUuid(SERVICE_UUID)
        val data = AdvertiseData.Builder()
            .setIncludeDeviceName(true)
            .addServiceUuid(parcelUuid)
            .build()

        mBluetoothLeAdvertiser.startAdvertising(settings, data, mAdvertiseCallback);
    }

    private val mAdvertiseCallback = object : AdvertiseCallback() {
        override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
            Log.d("T", "Peripheral advertising started.")
        }

        override fun onStartFailure(errorCode: Int) {
            Log.d("T", "Peripheral advertising failed: $errorCode")
        }
    }

    private fun startScan() {
        //runWithPermissions(ACCESS_FINE_LOCATION) {
        //requestBluetoothEnable()

        val filters = listOf<ScanFilter>()
        val settings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
            .build()

        mScanResults = hashMapOf()
        mScanCallback = BtleScanCallback()
        bluetoothLeScanner.startScan(mScanCallback)
        mScanning = true

        mHandler = Handler()
        mHandler.postDelayed({ stopScan() }, 10000)
        //}
    }

    private fun stopScan() {
        if (mScanning && bluetoothAdapter.isEnabled) {
            bluetoothLeScanner.stopScan(mScanCallback)
            scanComplete()
        }
        mScanning = false
    }

    private fun scanComplete() {
        if (mScanResults.isEmpty()) {
            return
        }
        for (deviceAddress in mScanResults.keys) {
            Log.d("T", "Found device: $deviceAddress")
        }
    }

    private inner class BtleScanCallback : ScanCallback() {

        override fun onScanResult(callbackType: Int, result: ScanResult) {
            addScanResult(result)
        }

        override fun onBatchScanResults(results: List<ScanResult>) {
            for (result in results) {
                addScanResult(result)
            }
        }

        override fun onScanFailed(errorCode: Int) {
            Log.e("T", "BLE Scan Failed with code $errorCode")
        }

        private fun addScanResult(result: ScanResult) {
            val device = result.device
            val deviceAddress = device.address
            val name = device.name
            if (name.equals("OnePlus 7")) {
                print("")
            }
            mScanResults[deviceAddress] = device
        }
    }

    private fun requestBluetoothEnable() {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        startActivityForResult(enableBtIntent, 123)
        Log.d(
            "T",
            "Requested user enables Bluetooth. Try starting the scan again."
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == 123) {
            startScan()
        }
    }

    private fun PackageManager.missingSystemFeature(name: String): Boolean = !hasSystemFeature(name)
}
