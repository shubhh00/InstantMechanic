package com.app.instantmechanic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.app.instantmechanic.navigation.InstantMechanicNavHost
import com.app.instantmechanic.ui.theme.InstantMechanicTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            InstantMechanicTheme {
                InstantMechanicNavHost()
            }
        }
    }
}