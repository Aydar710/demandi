package com.aydar.demandi.feature.create

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import com.aydar.demandi.EXTRA_ROOM_NAME
import com.aydar.demandi.ROOM_NAME_PREFIX
import com.aydar.demandi.base.BaseBluetoothActivity
import com.aydar.demandi.base.ServiceHolder
import com.aydar.demandi.feature.room.teacher.TeachersRoomActivity
import kotlinx.android.synthetic.main.activity_create_room.*

class CreateRoomActivity : BaseBluetoothActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.aydar.demandi.R.layout.activity_create_room)

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
        val intent = Intent(this, TeachersRoomActivity::class.java)
        intent.putExtra(EXTRA_ROOM_NAME, roomName)
        startActivity(intent)
    }

    private fun requestDiscoverable() {
        val discoverableIntent: Intent =
            Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
                putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 1000)
            }
        startActivityForResult(discoverableIntent, 12345)
    }
}
