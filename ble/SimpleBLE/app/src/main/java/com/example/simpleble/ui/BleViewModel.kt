package com.example.simpleble.ui

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.simpleble.BleApplication
import com.example.simpleble.data.BleRepository
import com.example.simpleble.data.ble.BleData
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class BleViewModel(
    private val bleRepository: BleRepository
) : ViewModel() {
    var notificationEnabled = mutableStateOf(false)
    val bleDataState = bleRepository.bleDataFlow
//    val bleDataState: StateFlow<BleData> = bleRepository.bleDataFlow.stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.WhileSubscribed(5_000),
//        initialValue = BleData()
//    )

    fun connect() {
        bleRepository.connect()
    }

    fun readCharacteristic() {
        bleRepository.readCharacteristic()
    }

    fun switchNotification() {
        notificationEnabled.value = !notificationEnabled.value
        bleRepository.setNotification(enabled = notificationEnabled.value)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as BleApplication)
                BleViewModel(application.bleRepository)
            }
        }
    }
}