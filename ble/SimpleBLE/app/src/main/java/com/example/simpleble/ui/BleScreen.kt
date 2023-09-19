package com.example.simpleble.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.simpleble.BleApplication
import com.example.simpleble.data.BleRepository
import com.example.simpleble.data.ble.BleData
import com.example.simpleble.data.ble.BleService

@Composable
fun BleApp() {
    val bleViewModel: BleViewModel = viewModel(factory = BleViewModel.Factory)
    val bleData: State<BleData> = bleViewModel.bleDataState.collectAsState(initial = BleData())
    BleScreen(
        bleData = bleData.value,
        bleViewModel = bleViewModel
    )
}

@Composable
fun BleScreen(
    bleData: BleData,
    bleViewModel: BleViewModel
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Connection state: ${bleData.connectState}")
        Spacer(modifier = Modifier.size(20.dp))
        Text("Device name: ${bleData.deviceName}")
        Spacer(modifier = Modifier.size(20.dp))
        Text("Characteristic UUID: ${bleData.characteristicUuid}")
        Spacer(modifier = Modifier.size(20.dp))
        Text("Value: ${bleData.characteristicValue}")
        Spacer(modifier = Modifier.size(20.dp))
        Button(onClick = { bleViewModel.connect() }) {
            Text(text = "Connect")
        }
        Spacer(modifier = Modifier.size(20.dp))
        Button(onClick = { bleViewModel.readCharacteristic() }) {
            Text(text = "Read")
        }
        Spacer(modifier = Modifier.size(20.dp))
        Button(onClick = { bleViewModel.switchNotification() }) {
            if (!bleViewModel.notificationEnabled.value)
                Text(text = "Subscribe")
            else
                Text(text = "Unsubscribe")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BleScreenPreview() {
    BleScreen(BleData(), BleViewModel(BleRepository(BleApplication())))
}