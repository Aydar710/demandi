package com.aydar.demandi.feature.join

import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aydar.demandi.R
import com.aydar.demandi.ROOM_NAME_PREFIX
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_room.view.*

class JoinAdapter(private val onDeviceClicked: (BluetoothDevice) -> Unit) :
    ListAdapter<BluetoothDevice, JoinAdapter.BluetoothDeviceViewHolder>(BluetoothDeviceDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BluetoothDeviceViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_room, parent, false)
        return BluetoothDeviceViewHolder(view)
    }

    override fun onBindViewHolder(holder: BluetoothDeviceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun addDevice(device: BluetoothDevice) {
        val newDevices = getAllDevices()
        newDevices.add(device)
        submitList(newDevices)
    }

    private fun getAllDevices(): MutableList<BluetoothDevice> {
        val allDevices = mutableListOf<BluetoothDevice>()
        for (i in 0 until itemCount) {
            allDevices.add(getItem(i))
        }
        return allDevices
    }


    inner class BluetoothDeviceViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bind(device: BluetoothDevice) {
            with(containerView) {
                tv_name.text = device.name.drop(ROOM_NAME_PREFIX.length)
            }

            containerView.setOnClickListener {
                onDeviceClicked.invoke(device)
            }
        }
    }
}