package com.aydar.feature_room_details

import android.os.Bundle
import com.aydar.demandi.common.base.BaseBluetoothActivity
import com.aydar.demandi.common.base.EXTRA_ROOM
import com.aydar.demandi.common.base.ROOM_NAME_PREFIX
import com.aydar.demandi.data.model.Room
import kotlinx.android.synthetic.main.activity_room_details.*
import org.koin.android.viewmodel.ext.android.viewModel

class RoomDetailsActivity : BaseBluetoothActivity() {

    private val viewModel: RoomDetailsViewModel by viewModel()
    private lateinit var room: Room

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_details)

        room = intent.getSerializableExtra(EXTRA_ROOM) as Room
        requestDiscoverable()

        fab_door.setOnClickListener {
            bluetoothAdapter?.name = "$ROOM_NAME_PREFIX${room.name}"
            viewModel.openRoom(room, this)
        }
    }
}
