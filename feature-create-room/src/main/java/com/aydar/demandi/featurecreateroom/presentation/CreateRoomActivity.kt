package com.aydar.demandi.featurecreateroom.presentation

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import com.aydar.demandi.common.base.BaseBluetoothActivity
import com.aydar.demandi.featurecreateroom.R
import com.aydar.demandi.featurecreateroom.ROOM_NAME_PREFIX
import kotlinx.android.synthetic.main.activity_create_room.*
import org.koin.android.viewmodel.ext.android.viewModel

class CreateRoomActivity : BaseBluetoothActivity() {

    private val viewModel: CreateRoomViewModel by viewModel()
    private lateinit var createRoomViewHolder: CreateRoomViewHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_room)
        createRoomViewHolder = CreateRoomViewHolder(this)

        requestDiscoverable()

        btn_create.setOnClickListener {
            val room = createRoomViewHolder.getRoom()
            bluetoothAdapter?.name = "$ROOM_NAME_PREFIX${room.name}"
            viewModel.onCreateBtnClicked(room, this)
        }
    }

    private fun requestDiscoverable() {
        val discoverableIntent: Intent =
            Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
                putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 1000)
            }
        startActivityForResult(discoverableIntent, 12345)
    }
}
