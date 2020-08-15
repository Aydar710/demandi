package com.aydar.demandi.common

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.aydar.demandi.common.BaseBluetoothActivity

class SharedPrefWrapper(context: Context) {


    private val sPref: SharedPreferences =
        context.getSharedPreferences(BLUETOOTH_ADDRESS, AppCompatActivity.MODE_PRIVATE)


    fun saveLastConnectedTeacherAddress(address: String) {
        sPref.edit().run {
            putString(BaseBluetoothActivity.BLUETOOTH_ADDRESS, address)
            apply()
        }
    }

    fun getLastConnectedTeacherAddress(): String? {
        val address = sPref.getString(BaseBluetoothActivity.BLUETOOTH_ADDRESS, "")
        return address
    }


    companion object {
        const val BLUETOOTH_ADDRESS = "bluetoothAddress"
    }
}