package com.example.flowpractice.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun StateFlowScreen() {
    val stateFlowViewModel: StateFlowViewModel = viewModel()
    val counter = stateFlowViewModel.counter.collectAsState()

    StateFlowScreen(
        counter = counter.value,
        viewModel = stateFlowViewModel
    )
}

@Composable
fun StateFlowScreen(
    counter: Int,
    viewModel: StateFlowViewModel
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { viewModel.incrementCounter() }) {
            Text(
                text = "$counter",
                fontSize = 50.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StateFlowScreenPreview() {
    StateFlowScreen()
}