package com.aydar.demandi.featurejoinroom

import android.app.Activity
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieDrawable
import com.airbnb.lottie.LottieTask
import com.aydar.demandi.common.base.*
import com.aydar.demandi.common.base.bluetooth.student.StudentServiceFacade
import kotlinx.android.synthetic.main.activity_join_room.*
import org.koin.android.ext.android.inject

class JoinRoomActivity : BaseBluetoothActivity() {

    private val studentServiceFacade: StudentServiceFacade by inject()

    private lateinit var adapter: JoinAdapter

    private lateinit var progressDialog: ProgressDialog

    private val router: JoinRoomRouter by inject()

    private var animateBluetoothIcon: LottieDrawable? = null
    private var bluetoothMenuItem: MenuItem? = null
    private lateinit var connectedDevice: BluetoothDevice
    private val sharedPref: SharedPrefWrapper by inject()

    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    Log.d("Bl", "Device found")
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val deviceName = device?.name
                    if (deviceName != null) {
                        if (deviceName.startsWith(ROOM_NAME_PREFIX)) {
                            adapter.addDevice(device)
                        }
                    }
                }
                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                    adapter.clear()
                    bluetoothMenuItem?.icon = animateBluetoothIcon
                    animateBluetoothIcon?.playAnimation()
                    Log.d("Bl", "Discovery started")
                    makeViewsGone()
                    supportActionBar?.title = getString(R.string.searching)
                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    animateBluetoothIcon?.endAnimation()
                    bluetoothMenuItem?.setIcon(R.drawable.ic_bluetooth_search)
                    supportActionBar?.title = getString(R.string.choose_room)
                    if (adapter.checkIfListIsEmpty()) {
                        makeViewsVisible()
                    }
                    Log.d("Bl", "Discovery finished")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_room)

        initToolbar()
        initRecycler()
        initHandler()
        registerFoundReceiver()
        registerBondStateReceiver()
        btn_repeat.setOnClickListener {
            bluetoothAdapter.startDiscovery()
        }

        requestTurnOnBluetooth()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_join_room, menu)
        val lottieTask: LottieTask<LottieComposition> =
            LottieCompositionFactory.fromAsset(this, "bluetooth_search_animation.json")

        lottieTask.addListener {
            animateBluetoothIcon = LottieDrawable()
            animateBluetoothIcon?.composition = it
            animateBluetoothIcon?.repeatCount = LottieDrawable.INFINITE
            animateBluetoothIcon?.scale = 0.07f
            animateBluetoothIcon?.speed = 0.8f
            val bluetoothItem = menu?.findItem(R.id.action_search)
            bluetoothMenuItem = bluetoothItem
            bluetoothAdapter.startDiscovery()
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
                bluetoothAdapter.startDiscovery()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_TURN_ON_BLUETOOTH) {
            if (resultCode == Activity.RESULT_OK) {
                bluetoothAdapter.startDiscovery()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.can_not_continue_without_bluetooth),
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun initHandler(): Handler {
        return Handler {
            when (it.what) {
                MESSAGE_SHOW_DIALOG -> {
                    showProgress();
                    true
                }
                MESSAGE_HIDE_DIALOG -> {
                    hideProgress()
                    true
                }
                MESSAGE_ERROR_WHILE_CONNECT -> {
                    hideProgress()
                    Toast.makeText(
                        this,
                        getString(R.string.connection_not_established),
                        Toast.LENGTH_SHORT
                    ).show()
                    true
                }
                MESSAGE_CONNECTED_TO_ROOM -> {
                    startStudentsRoomActivity(connectedDevice.name.drop(ROOM_NAME_PREFIX.length))
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

    private fun makeViewsGone() {
        tv_no_devices.visibility = View.GONE
        btn_repeat.visibility = View.GONE
    }

    private fun makeViewsVisible() {
        tv_no_devices.visibility = View.VISIBLE
        btn_repeat.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        progressDialog.dismiss()
    }

    private fun registerFoundReceiver() {
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
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
                        }
                    }
                }
            }
        }
        val filter = IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        registerReceiver(receiver, filter)
    }

    private fun connectToDevice(device: BluetoothDevice) {
        connectedDevice = device
        val handler = initHandler()
        studentServiceFacade.startConnecting(connectedDevice, handler)
    }

    private fun startStudentsRoomActivity(roomName: String) {
        router.moveToStudentsRoomActivityWithName(this, roomName)
    }

    private fun initRecycler() {
        adapter = JoinAdapter {
            sharedPref.saveLastConnectedTeacherAddress(it.address)
            if (it.bondState == BluetoothDevice.BOND_BONDED) {
                connectToDevice(it)
            } else {
                it.createBond()
            }
        }
        rv_rooms.adapter = adapter
    }

    private fun initToolbar() {
        val toolbar = inc_toolbar as Toolbar
        toolbar.setBackgroundColor(Color.WHITE)
        toolbar.title = getString(R.string.choose_room)
        toolbar.setTitleTextColor(Color.BLACK)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onDestroy() {
        super.onDestroy()

        unregisterReceiver(receiver)
        bluetoothAdapter.cancelDiscovery()
    }
}
