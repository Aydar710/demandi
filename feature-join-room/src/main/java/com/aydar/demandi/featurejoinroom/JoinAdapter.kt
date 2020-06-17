package com.aydar.demandi.featurejoinroom

import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aydar.demandi.common.base.getRoomNameFromDeviceName
import com.aydar.demandi.common.base.getSubjectNameFromDeviceName
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_join_room.view.*

class JoinAdapter(private val onDeviceClicked: (BluetoothDevice) -> Unit) :
    ListAdapter<BluetoothDevice, JoinAdapter.BluetoothDeviceViewHolder>(BluetoothDeviceDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BluetoothDeviceViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_join_room, parent, false)
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

    fun checkIfListIsEmpty(): Boolean = itemCount == 0

    fun clear() {
        submitList(emptyList())
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
                tv_name.text = getRoomNameFromDeviceName(device.name)
                tv_subject_name.text = getSubjectNameFromDeviceName(device.name)
            }

            containerView.setOnClickListener {
                onDeviceClicked.invoke(device)
            }
        }
    }
}