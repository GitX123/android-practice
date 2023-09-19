package com.example.simpleble.data.ble

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.example.simpleble.BleApplication

private val TAG = "GattUpdateReceiver"

class GattUpdateReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        val application = context?.applicationContext as BleApplication

        when (intent.action) {
            BleService.ACTION_GATT_CONNECTED -> {
                application.bleData = BleData(connectState = "connected")
                Log.d(TAG, "[ACTION_GATT_CONNECTED] ${application.bleData}")
            }
            BleService.ACTION_GATT_DISCONNECTED -> {
                application.bleData = BleData()
                Log.d(TAG, "[ACTION_GATT_DISCONNECTED] ${application.bleData}")
            }
            BleService.ACTION_GATT_SERVICES_DISCOVERED -> {
                Log.d(TAG, "[ACTION_GATT_SERVICES_DISCOVERED]")
            }
            BleService.ACTION_DATA_AVAILABLE -> {
                intent.extras?.let { extras ->
                    application.bleData = BleData().apply {
                        connectState = "connected"
                        deviceName = application.targetBleDeviceName
                        characteristicUuid = application.targetCharacteristicUuid
                        characteristicValue =
                            extras.getString(BleService.EXTRA_DATA).toString()
                    }
                }
                Log.d(TAG, "[ACTION_DATA_AVAILABLE] ${application.bleData}")
            }
        }
    }

    // Intent filter for GATT broadcast receiver
    fun makeGattUpdateIntentFilter(): IntentFilter? {
        return IntentFilter().apply {
            this.addAction(BleService.ACTION_GATT_CONNECTED)
            this.addAction(BleService.ACTION_GATT_DISCONNECTED)
            this.addAction(BleService.ACTION_GATT_SERVICES_DISCOVERED)
            this.addAction(BleService.ACTION_DATA_AVAILABLE)
        }
    }
}