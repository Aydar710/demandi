package com.aydar.demandi.featurecreateroom.presentation

import android.os.Bundle
import com.aydar.demandi.common.base.BaseBluetoothActivity
import com.aydar.demandi.common.base.createDeviceName
import com.aydar.demandi.featurecreateroom.R
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
            sharedPrefWrapperDeviceName.saveDeviceName(bluetoothAdapter.name)
            bluetoothAdapter.name = createDeviceName(room)
            viewModel.onCreateBtnClicked(room, this)
        }
    }
}
