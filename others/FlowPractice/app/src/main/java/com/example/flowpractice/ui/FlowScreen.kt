package com.example.flowpractice.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun FlowScreen() {
    val flowViewModel: FlowViewModel = viewModel()
    val count = flowViewModel.countDownFlow.collectAsState(initial = flowViewModel.initialValue)

    FlowScreen(
        count = count.value,
        modifier = Modifier
    )
}

@Composable
fun FlowScreen(
    count: Int,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Text(
            text = "$count",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FlowScreenPreview() {
    FlowScreen()
}