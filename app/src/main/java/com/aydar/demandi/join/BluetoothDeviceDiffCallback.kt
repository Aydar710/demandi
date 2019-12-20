package com.aydar.demandi.join

import android.bluetooth.BluetoothDevice
import androidx.recyclerview.widget.DiffUtil

class BluetoothDeviceDiffCallback : DiffUtil.ItemCallback<BluetoothDevice>() {

    override fun areItemsTheSame(oldItem: BluetoothDevice, newItem: BluetoothDevice): Boolean =
        oldItem.address == newItem.address

    override fun areContentsTheSame(oldItem: BluetoothDevice, newItem: BluetoothDevice): Boolean =
        oldItem == newItem
}