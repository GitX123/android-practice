package com.example.amphibians.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.amphibians.ui.screens.AmphibiansViewModel
import com.example.amphibians.ui.screens.HomeScreen

@Composable
fun AmphibiansApp() {
    val amphibiansViewModel: AmphibiansViewModel = viewModel(factory = AmphibiansViewModel.Factory)
    HomeScreen(
        uiState = amphibiansViewModel.uiState,
        retryAction = amphibiansViewModel::getAmphibians
    )
}