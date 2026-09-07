package com.app.instantmechanic.screens

import androidx.activity.compose.ReportDrawnWhen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.app.instantmechanic.MechanicViewModel

@Composable
fun MechanicScreen(
    modifier: Modifier = Modifier,
    viewModel: MechanicViewModel,
    onMechanicClick: (String) -> Unit,
    onVideoConsultationClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    ReportDrawnWhen {
        !state.isInitialLoading
    }

    when {
        state.isInitialLoading && state.mechanics.isEmpty() -> {
            InitialLoadingContent(modifier)
        }

        state.errorMessage != null && state.mechanics.isEmpty() -> {
            InitialErrorContent(
                modifier = modifier,
                message = state.errorMessage.orEmpty(),
                onRetry = viewModel::refreshMechanics
            )
        }

        else -> {
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
                item {
                    VideoConsultationCard(
                        onStartConsultation =
                            onVideoConsultationClick
                    )
                }
                if (state.isRefreshing) {
                    item {
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color(0xFFFF6B0B)
                        )
                    }
                }

                state.errorMessage?.let { message ->
                    item {
                        OfflineBanner(
                            message = message,
                            onRetry = viewModel::refreshMechanics
                        )
                    }
                }

                if (state.mechanics.isEmpty()) {
                    item {
                        Text(
                            text = "No mechanics are currently available.",
                            color = Color(0xFF746A63)
                        )
                    }
                }

                items(
                    items = state.mechanics,
                    key = { mechanic -> mechanic.id }
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

@Composable
private fun InitialLoadingContent(
    modifier: Modifier
) {
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

@Composable
private fun InitialErrorContent(
    modifier: Modifier,
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFFFBF3))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = message,
            color = Color(0xFF746A63)
        )

        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF6B0B)
            )
        ) {
            Text("Retry")
        }
    }
}

@Composable
private fun OfflineBanner(
    message: String,
    onRetry: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFFFF0E6)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = message,
                color = Color(0xFF746A63)
            )

            TextButton(
                onClick = onRetry
            ) {
                Text(
                    text = "Try again",
                    color = Color(0xFFFF6B0B)
                )
            }
        }
    }
}