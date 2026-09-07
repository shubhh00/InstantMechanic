package com.app.instantmechanic.video

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun VideoConsultationScreen(
    viewModel: VideoCallViewModel,
    onCallFinished: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val callIsActive =
        uiState.phase == VideoCallPhase.JOINING ||
                uiState.phase == VideoCallPhase.WAITING_FOR_MECHANIC ||
                uiState.phase == VideoCallPhase.CONNECTED

    /*
     * Pressing Android's system back button during a call
     * must leave the Agora channel before navigating away.
     */
    BackHandler(enabled = callIsActive) {
        viewModel.endCall()
    }

    /*
     * Navigation remains a UI responsibility. The ViewModel
     * reports ENDED, and the composable performs navigation.
     */
    LaunchedEffect(uiState.phase) {
        if (uiState.phase == VideoCallPhase.ENDED) {
            onCallFinished()
        }
    }

    when (uiState.phase) {
        VideoCallPhase.PREPARING -> {
            VideoConsultationPreCallScreen(
                onSetupLocalVideo = viewModel::setupLocalVideo,
                onStartCall = viewModel::startCall,
                onBack = viewModel::endCall
            )
        }

        VideoCallPhase.JOINING,
        VideoCallPhase.WAITING_FOR_MECHANIC,
        VideoCallPhase.CONNECTED -> {
            ActiveVideoCallScreen(
                uiState = uiState,
                onSetupLocalVideo = viewModel::setupLocalVideo,
                onSetupRemoteVideo = viewModel::setupRemoteVideo,
                onToggleMicrophone = viewModel::toggleMicrophone,
                onToggleCamera = viewModel::toggleCamera,
                onSwitchCamera = viewModel::switchCamera,
                onEndCall = viewModel::endCall
            )
        }

        VideoCallPhase.ERROR -> {
            VideoCallErrorContent(
                message = uiState.errorMessage
                    ?: "Unable to start video consultation",
                onRetry = viewModel::startCall,
                onClose = viewModel::endCall
            )
        }

        VideoCallPhase.ENDED -> {
            // LaunchedEffect navigates away immediately.
        }
    }
}

@Composable
private fun VideoCallErrorContent(
    message: String,
    onRetry: () -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFBF3))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Video consultation unavailable",
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1C1917)
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = message,
            color = Color(0xFF746A63),
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Button(
            onClick = onRetry,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF6B0B)
            )
        ) {
            Text("Try again")
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        OutlinedButton(
            onClick = onClose,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Close")
        }
    }
}