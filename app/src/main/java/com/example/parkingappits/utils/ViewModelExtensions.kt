package com.example.parkingappits.presentation.viewmodel

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
inline fun <reified T : ViewModel> viewModelFactory(factory: ViewModelProvider.Factory): T {
    return viewModel(factory = factory)
}
