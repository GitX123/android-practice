package com.example.simpleble

import android.app.Application
import com.example.simpleble.data.BleRepository
import com.example.simpleble.data.ble.BleData
import com.example.simpleble.data.ble.BleService
import com.example.simpleble.data.ble.GattUpdateReceiver

class BleApplication: Application() {
    // BLE
    val targetBleDeviceName = "Arduino"
    val targetServiceUuid = "00000020-0000-1000-8000-00805f9b34fb"
    val targetCharacteristicUuid = "00000021-0000-1000-8000-00805f9b34fb"
    val targetDescriptorUuid = "00002902-0000-1000-8000-00805F9B34FB"

    var bleService : BleService? = null
    var bleData = BleData()
    val gattUpdateReceiver = GattUpdateReceiver()
    lateinit var bleRepository: BleRepository

    override fun onCreate() {
        super.onCreate()
        bleRepository = BleRepository(this)
    }
}