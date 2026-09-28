package com.app.instantmechanic

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import com.app.instantmechanic.navigation.InstantMechanicNavHost
import com.app.instantmechanic.ui.theme.InstantMechanicTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var notificationMechanicId by mutableStateOf<String?>(null)

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            // The app still works if the user declines; notifications won't be shown.
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        readNavigationTarget(intent)
        requestNotificationPermission()
        enableEdgeToEdge()

        setContent {
            InstantMechanicTheme {
                InstantMechanicNavHost(
                    notificationMechanicId = notificationMechanicId,
                    onNotificationHandled = {
                        notificationMechanicId = null
                    })
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        readNavigationTarget(intent)
    }

    private fun readNavigationTarget(incomingIntent: Intent?) {
        if (incomingIntent == null) return

        val notificationId =
            incomingIntent.getStringExtra("mechanic_id")?.takeIf { it.isNotBlank() }

        val linkId = if (incomingIntent.action == Intent.ACTION_VIEW) {
            incomingIntent.data?.takeIf {
                it.scheme == "instantmechanic" && it.host == "mechanic"
            }?.pathSegments?.singleOrNull()?.takeIf { it.isNotBlank() }
        } else {
            null
        }

        val mechanicId = notificationId ?: linkId ?: return

        // Consume either source so Activity recreation won't navigate again.
        incomingIntent.removeExtra("mechanic_id")
        if (linkId != null) {
            incomingIntent.data = null
        }

        notificationMechanicId = mechanicId
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            notificationPermissionLauncher.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )
        }
    }
}