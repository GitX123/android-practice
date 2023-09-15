package com.example.flowpractice.ui

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class FlowViewModel : ViewModel() {
    val initialValue = 10
    val countDownFlow = flow<Int> {
        var count = initialValue
        emit(count)
        while (count > 0) {
            delay(1000L)
            count--
            emit(count)
        }
    }

    private fun collectFlow() {
        viewModelScope.launch {
            countDownFlow
                .filter {
                    it % 2 == 0
                }
                .map {
                    it / 2
                }
                .collect {
                    println("Count: $it")
                }
        }
    }
}
