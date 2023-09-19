package com.example.simpleble.data.ble

data class BleData(
    var connectState: String = "disconnected",
    var deviceName: String = "",
    var characteristicUuid: String = "",
    var characteristicValue: String = ""
)