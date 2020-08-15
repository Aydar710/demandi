package com.aydar.demandi.common

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity

class SharedPrefWrapperDeviceName(context: Context) {

    private val sPref: SharedPreferences =
        context.getSharedPreferences(SHARED_PREF_FILENAME, AppCompatActivity.MODE_PRIVATE)


    fun saveDeviceName(deviceName: String) {
        sPref.edit().run {
            putString(KEY_DEVICE_NAME, deviceName)
            apply()
        }
    }

    fun getDeviceName(): String? = sPref.getString(KEY_DEVICE_NAME, null)

    fun deleteDeviceName() = sPref.edit().remove(KEY_DEVICE_NAME).apply()

    companion object {
        const val SHARED_PREF_FILENAME = "demandiSPref"
        const val KEY_DEVICE_NAME = "device name"
    }
}