package com.aydar.demandi.featurecreateroom

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import com.aydar.demandi.common.base.BaseBluetoothActivity
import com.aydar.demandi.common.base.bluetooth.ServiceHolder
import kotlinx.android.synthetic.main.activity_create_room.*
import org.koin.android.ext.android.inject

class CreateRoomActivity : BaseBluetoothActivity() {

    private val router: CreateRoomRouter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_room)

        requestDiscoverable()

        btn_create.setOnClickListener {
            val roomName = et_name.text.toString()
            if (roomName.isNotEmpty()) {
                ServiceHolder.teacherService.startServer()
                bluetoothAdapter?.name = "$ROOM_NAME_PREFIX$roomName"
                startTeacherRoomActivity(roomName)
            }
        }
    }

    private fun startTeacherRoomActivity(roomName: String) {
        router.moveToTeacherRoomActivity(this, roomName)
    }

    private fun requestDiscoverable() {
        val discoverableIntent: Intent =
            Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
                putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 1000)
            }
        startActivityForResult(discoverableIntent, 12345)
    }
}
