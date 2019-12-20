package com.aydar.demandi.create

import android.bluetooth.BluetoothSocket
import androidx.lifecycle.ViewModel

class TeachersViewModel : ViewModel() {

    private val students = mutableListOf<BluetoothSocket>()

    fun addStudent(socket: BluetoothSocket) {
        students.add(socket)
    }
}