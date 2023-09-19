package com.example.simpleble

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.example.simpleble.data.BleRepository
import com.example.simpleble.data.ble.BleService
import com.example.simpleble.ui.BleApp
import com.example.simpleble.ui.theme.SimpleBLETheme

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // BLE ServiceConnection
        val application = applicationContext as BleApplication
        val serviceConnection: ServiceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                application.bleService = (service as BleService.LocalBinder).getService()
                application.bleService?.let { ble ->
                    // initialize BLE
                    if (!ble.initialize()) {
                        Log.e(TAG, "Unable to initialize BLE")
                        finish()
                    }
                    // connect to GATT
                    ble.connect(application.targetBleDeviceName)
                }
            }
            override fun onServiceDisconnected(name: ComponentName?) {
                application.bleService = null
            }
        }

        // bind to the BleService
        val bleServiceIntent = Intent(this, BleService::class.java)
        bindService(bleServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE)

        // set up screen
        setContent {
            SimpleBLETheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BleApp()
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        val application = applicationContext as BleApplication
        registerReceiver(application.gattUpdateReceiver, application.gattUpdateReceiver.makeGattUpdateIntentFilter())
    }

    override fun onPause() {
        super.onPause()
        val application = applicationContext as BleApplication
        unregisterReceiver(application.gattUpdateReceiver)
    }
}