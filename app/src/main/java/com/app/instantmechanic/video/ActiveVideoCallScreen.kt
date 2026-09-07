package com.app.instantmechanic.video

import android.view.SurfaceView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun ActiveVideoCallScreen(
    uiState: VideoCallUiState,
    onSetupLocalVideo: (SurfaceView) -> Unit,
    onSetupRemoteVideo: (SurfaceView, Int) -> Unit,
    onToggleMicrophone: () -> Unit,
    onToggleCamera: () -> Unit,
    onSwitchCamera: () -> Unit,
    onEndCall: () -> Unit
) {
    val remoteUid = uiState.remoteUid

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (remoteUid == null) {
            WaitingVideoContent(
                cameraEnabled = uiState.isCameraEnabled,
                onSetupLocalVideo = onSetupLocalVideo
            )
        } else {
            key(remoteUid) {
                RemoteVideoSurface(
                    remoteUid = remoteUid,
                    onSetupRemoteVideo = onSetupRemoteVideo
                )
            }

            LocalVideoOverlay(
                cameraEnabled = uiState.isCameraEnabled,
                onSetupLocalVideo = onSetupLocalVideo,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .statusBarsPadding()
                    .padding(16.dp)
            )
        }

        CallStatusLabel(
            phase = uiState.phase,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 18.dp)
        )

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(
                    horizontal = 20.dp,
                    vertical = 32.dp
                ),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CallControlButton(
                icon = if (uiState.isMicrophoneEnabled) {
                    Icons.Filled.Mic
                } else {
                    Icons.Filled.MicOff
                },
                contentDescription = if (uiState.isMicrophoneEnabled) {
                    "Mute microphone"
                } else {
                    "Unmute microphone"
                },
                containerColor = Color(0xFF302E2C),
                onClick = onToggleMicrophone
            )

            CallControlButton(
                icon = if (uiState.isCameraEnabled) {
                    Icons.Filled.Videocam
                } else {
                    Icons.Filled.VideocamOff
                },
                contentDescription = if (uiState.isCameraEnabled) {
                    "Turn camera off"
                } else {
                    "Turn camera on"
                },
                containerColor = Color(0xFF302E2C),
                onClick = onToggleCamera
            )

            CallControlButton(
                icon = Icons.Filled.Cameraswitch,
                contentDescription = "Switch camera",
                containerColor = Color(0xFF302E2C),
                onClick = onSwitchCamera
            )

            CallControlButton(
                icon = Icons.Filled.CallEnd,
                contentDescription = "End call",
                containerColor = Color(0xFFE53935),
                onClick = onEndCall
            )
        }
    }
}

@Composable
private fun WaitingVideoContent(
    cameraEnabled: Boolean,
    onSetupLocalVideo: (SurfaceView) -> Unit
) {
    if (cameraEnabled) {
        LocalVideoSurface(
            onSetupLocalVideo = onSetupLocalVideo,
            modifier = Modifier.fillMaxSize()
        )
    } else {
        CameraDisabledContent(
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun RemoteVideoSurface(
    remoteUid: Int,
    onSetupRemoteVideo: (SurfaceView, Int) -> Unit
) {
    AndroidView(
        factory = { context ->
            SurfaceView(context).also { surfaceView ->
                onSetupRemoteVideo(
                    surfaceView,
                    remoteUid
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun LocalVideoSurface(
    onSetupLocalVideo: (SurfaceView) -> Unit,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { context ->
            SurfaceView(context).also { surfaceView ->
                onSetupLocalVideo(surfaceView)
            }
        },
        modifier = modifier
    )
}

@Composable
private fun LocalVideoOverlay(
    cameraEnabled: Boolean,
    onSetupLocalVideo: (SurfaceView) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .size(
                width = 110.dp,
                height = 160.dp
            ),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFF252525),
        shadowElevation = 8.dp
    ) {
        if (cameraEnabled) {
            AndroidView(
                factory = { context ->
                    SurfaceView(context).also { surfaceView ->
                        /*
                         * This SurfaceView must be drawn above the
                         * full-screen remote SurfaceView.
                         */
                        surfaceView.setZOrderMediaOverlay(true)

                        onSetupLocalVideo(surfaceView)
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
            )
        } else {
            CameraDisabledContent()
        }
    }
}

@Composable
private fun CameraDisabledContent(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(Color(0xFF252525)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.VideocamOff,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(40.dp)
        )
    }
}

@Composable
private fun CallStatusLabel(
    phase: VideoCallPhase,
    modifier: Modifier = Modifier
) {
    val status = when (phase) {
        VideoCallPhase.JOINING ->
            "Connecting…"

        VideoCallPhase.WAITING_FOR_MECHANIC ->
            "Waiting for a mechanic…"

        VideoCallPhase.CONNECTED ->
            "Connected"

        else ->
            null
    }

    if (status != null) {
        Surface(
            modifier = modifier,
            color = Color.Black.copy(alpha = 0.55f),
            shape = RoundedCornerShape(50)
        ) {
            Text(
                text = status,
                color = Color.White,
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
            )
        }
    }
}

@Composable
private fun CallControlButton(
    icon: ImageVector,
    contentDescription: String,
    containerColor: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.size(56.dp),
        shape = CircleShape,
        color = containerColor
    ) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = Color.White
            )
        }
    }
}