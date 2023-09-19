package com.example.simpleble.data


import android.util.Log
import com.example.simpleble.BleApplication
import com.example.simpleble.data.ble.BleData
import com.example.simpleble.data.ble.BleService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

private const val TAG = "BleRepository"

class BleRepository(
    private val application: BleApplication
) {
    private val refreshInterval = 100L
    val bleDataFlow: Flow<BleData> = flow {
        while (true) {
            emit(application.bleData)
            delay(refreshInterval)
        }
    }

    fun connect() {
        Log.d(TAG, "Try connecting to BLE device, bleService = $application.bleService")
        application.bleService?.connect(application.targetBleDeviceName)
    }

    fun readCharacteristic() {
        application.bleService?.readCharacteristic(application.targetServiceUuid, application.targetCharacteristicUuid)
    }

    fun setNotification(enabled: Boolean) {
        application.bleService?.setCharacteristicNotification(
            application.targetServiceUuid,
            application.targetCharacteristicUuid,
            application.targetDescriptorUuid,
            enabled
        )
    }
}