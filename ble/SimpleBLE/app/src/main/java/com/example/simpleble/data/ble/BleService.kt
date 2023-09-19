package com.example.simpleble.data.ble

import android.annotation.SuppressLint
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

private const val TAG = "BleService"

// BLE bound service
@SuppressLint("MissingPermission")
class BleService : Service() {
    private val binder = LocalBinder()
    private var bluetoothAdapter: BluetoothAdapter? = null
    private var bluetoothGatt: BluetoothGatt? = null
    private val bluetoothGattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                broadcastUpdate(ACTION_GATT_CONNECTED)
                bluetoothGatt?.discoverServices()?.let {
                    if (it) {
                        Log.d(TAG, "Service discovery started")
                    } else {
                        Log.d(TAG, "Service discovery stopped")
                    }
                }
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                broadcastUpdate(ACTION_GATT_DISCONNECTED)
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED)
            } else {
                Log.w(TAG, "onServicesDiscovered received: $status")
            }
        }

        // for API level < 33
        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.i(TAG, "onCharacteristicRead value: ${characteristic.value}")
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic.value)
            } else {
                Log.w(TAG, "onCharacteristicRead status: $status")
            }
        }

        // for API level >= 33
//        override fun onCharacteristicRead(
//            gatt: BluetoothGatt,
//            characteristic: BluetoothGattCharacteristic,
//            value: ByteArray,
//            status: Int
//        ) {
//            if (status == BluetoothGatt.GATT_SUCCESS) {
//                Log.i(TAG, "onCharacteristicRead value: $value")
//                broadcastUpdate(ACTION_DATA_AVAILABLE, value)
//            } else {
//                Log.w(TAG, "onCharacteristicRead status: $status")
//            }
//        }

        // for API level < 33
        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
        ) {
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic.value)
        }

        // for API level >= 33
//        override fun onCharacteristicChanged(
//            gatt: BluetoothGatt,
//            characteristic: BluetoothGattCharacteristic,
//            value: ByteArray
//        ) {
//            broadcastUpdate(ACTION_DATA_AVAILABLE, value)
//        }
    }

    inner class LocalBinder : Binder() {
        fun getService() : BleService {
            return this@BleService
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d(TAG, "Bind BLE service")
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG, "Unbind BLE service")
        close()
        return super.onUnbind(intent)
    }

    fun initialize(): Boolean {
        val bluetoothManager = applicationContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        if (bluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.")
            return false
        }
        Log.d(TAG, "BluetoothAdapter successfully initialized")
        return true
    }

    fun connect(deviceName: String): Boolean {
        bluetoothAdapter?.let { adapter ->
            val device = adapter.bondedDevices.find { device ->
                device.name == deviceName
            }
            if (device != null) {
                bluetoothGatt = device.connectGatt(applicationContext, false, bluetoothGattCallback)
                Log.i(TAG, "Connected to device successfully, bluetoothGatt = $bluetoothGatt")
                return true
            } else {
                Log.w(TAG, "Device not found with provided device name")
                return  false
            }
        } ?: run {
            Log.w(TAG, "BluetoothAdapter not initialized")
            return false
        }
    }

    private fun close() {
        bluetoothGatt?.let { gatt ->
            gatt.close()
            bluetoothGatt = null
        }
    }

    private fun broadcastUpdate(action: String) {
        val intent = Intent(action)
        sendBroadcast(intent)
    }

    private fun broadcastUpdate(action: String, value: ByteArray) {
        val intent = Intent(action)

        // parse characteristic
        if (value.isNotEmpty()) {
            val hexString: String = value.joinToString(separator = " ") {
                String.format("%02X", it)
            }
            intent.putExtra(EXTRA_DATA, hexString)
        }

        sendBroadcast(intent)
    }

    fun getGattServices(): List<BluetoothGattService?>? {
        return bluetoothGatt?.services
    }

    private fun getCharacteristic(serviceUuid: String, characteristicUuid: String): BluetoothGattCharacteristic? {
        return bluetoothGatt?.let { gatt ->
            gatt.services.find { service ->
                service.uuid == UUID.fromString(serviceUuid)
            }?.characteristics?.find { characteristic ->
                characteristic.uuid == UUID.fromString(characteristicUuid)
            }
        } ?: run {
            Log.w(TAG, "BluetoothGatt not initialized")
            null
        }
    }

    fun readCharacteristic(serviceUuid: String, characteristicUuid: String) {
        val characteristic = getCharacteristic(serviceUuid, characteristicUuid)
        characteristic?.let {
            readCharacteristic(it)
        } ?: run {
            Log.w(TAG, "Characteristic specified not found")
        }
    }

    private fun readCharacteristic(characteristic: BluetoothGattCharacteristic) {
        bluetoothGatt?.let {
            if (it.readCharacteristic(characteristic)) {
                Log.i(TAG, "Reading BLE characteristic")
            } else {
                Log.w(TAG, "Characteristic read operation failed")
            }
        } ?: run {
            Log.w(TAG, "BluetoothGatt not initialized")
        }
    }

    fun setCharacteristicNotification(serviceUuid: String, characteristicUuid: String, descriptorUuid: String, enabled: Boolean) {
        val characteristic = getCharacteristic(serviceUuid, characteristicUuid)
        characteristic?.let { characteristic ->
            setCharacteristicNotification(characteristic, descriptorUuid, enabled)
        } ?: run {
            Log.w(TAG, "BluetoothGatt not initialized")
        }
    }

    // for API level < 33
    private fun setCharacteristicNotification(characteristic: BluetoothGattCharacteristic, descriptorUuid: String, enabled: Boolean) {
        bluetoothGatt?.let { gatt ->
            gatt.setCharacteristicNotification(characteristic, enabled)
            val descriptor = characteristic.getDescriptor(UUID.fromString(descriptorUuid))
            descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            gatt.writeDescriptor(descriptor)
            // for API level >= 33
//            bluetoothGatt?.writeDescriptor(descriptor, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
        } ?: run {
            Log.w(TAG, "BluetoothGatt not initialized")
        }
    }

    companion object {
        const val ACTION_GATT_CONNECTED = "BLE.ACTION_GATT_CONNECTED"
        const val ACTION_GATT_DISCONNECTED = "BLE.ACTION_GATT_DISCONNECTED"
        const val ACTION_GATT_SERVICES_DISCOVERED = "BLE.ACTION_GATT_SERVICES_DISCOVERED"
        const val ACTION_DATA_AVAILABLE = "BLE.ACTION_DATA_AVAILABLE"
        const val EXTRA_DATA = "BLE.EXTRA_DATA"
    }
}