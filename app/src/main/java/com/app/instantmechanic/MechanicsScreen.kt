package com.app.instantmechanic

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun MechanicScreen(
    modifier: Modifier = Modifier,
    viewModel: MechanicViewModel = hiltViewModel()
) {
    val mechanics by viewModel.mechanics.collectAsState()

    Column(
        modifier = modifier
    ) {
        Text("Mechanics loaded: ${mechanics.size}")

        mechanics.forEach { mechanic ->
            Text(mechanic.name)
        }
    }
}