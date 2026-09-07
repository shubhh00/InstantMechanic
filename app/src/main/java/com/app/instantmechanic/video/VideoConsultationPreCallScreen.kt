package com.app.instantmechanic.video

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.view.SurfaceView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat

private val requiredVideoPermissions = arrayOf(
    Manifest.permission.CAMERA,
    Manifest.permission.RECORD_AUDIO
)

@Composable
fun VideoConsultationPreCallScreen(
    onSetupLocalVideo: (SurfaceView) -> Unit,
    onStartCall: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    var permissionsGranted by remember(context) {
        mutableStateOf(
            context.hasVideoCallPermissions()
        )
    }

    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissionResults ->

            permissionsGranted =
                permissionResults[Manifest.permission.CAMERA] == true &&
                        permissionResults[Manifest.permission.RECORD_AUDIO] == true

            if (!permissionsGranted) {
                onBack()
            }
        }

    LaunchedEffect(Unit) {
        if (!permissionsGranted) {
            permissionLauncher.launch(
                requiredVideoPermissions
            )
        }
    }

    if (!permissionsGranted) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        )

        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFBF3))
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            TextButton(onClick = onBack) {
                Text(
                    text = "← Back",
                    color = Color(0xFFFF6B0B)
                )
            }
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = "Video consultation",
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1C1917)
        )

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        Text(
            text = "Make sure the vehicle issue is visible before connecting.",
            color = Color(0xFF746A63)
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        AndroidView(
            factory = { viewContext ->
                SurfaceView(viewContext).also { surfaceView ->
                    onSetupLocalVideo(surfaceView)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3f / 4f)
                .clip(RoundedCornerShape(20.dp))
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

        Button(
            onClick = onStartCall,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF6B0B)
            )
        ) {
            Text("Connect with a mechanic")
        }
    }
}

private fun Context.hasVideoCallPermissions(): Boolean {
    val cameraGranted =
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

    val microphoneGranted =
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED

    return cameraGranted && microphoneGranted
}