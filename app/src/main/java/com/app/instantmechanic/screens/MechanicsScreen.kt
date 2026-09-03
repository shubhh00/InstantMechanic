package com.app.instantmechanic.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.app.instantmechanic.MechanicUiState
import com.app.instantmechanic.MechanicViewModel

@Composable
fun MechanicScreen(
    modifier: Modifier = Modifier,
    viewModel: MechanicViewModel,
    onMechanicClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {

        MechanicUiState.Loading -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(Color(0xFFFFFBF3)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color(0xFFFF6B0B)
                )
            }
        }

        is MechanicUiState.Error -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(Color(0xFFFFFBF3))
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = state.message,
                    color = Color(0xFF746A63)
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Button(
                    onClick = {
                        viewModel.loadMechanics()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF6B0B)
                    )
                ) {
                    Text("Retry")
                }
            }
        }

        is MechanicUiState.Success -> {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .background(Color(0xFFFFFBF3)),
                contentPadding = PaddingValues(
                    horizontal = 16.dp,
                    vertical = 20.dp
                ),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                item {
                    MechanicHomeHeader(
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }

                items(
                    items = state.mechanics,
                    key = { it.id }
                ) { mechanic ->

                    MechanicCard(
                        mechanic = mechanic,
                        onClick = {
                            onMechanicClick(mechanic.id)
                        }
                    )
                }
            }
        }
    }
}