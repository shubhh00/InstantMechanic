package com.app.instantmechanic.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import com.app.instantmechanic.R
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(1300.milliseconds)
        onSplashFinished()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFBF3)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(
                id = R.drawable.instant_mechanic_logo
            ),
            contentDescription = "Instant Mechanic",
            modifier = Modifier
                .fillMaxWidth(0.65f)
                .wrapContentHeight(),
            contentScale = ContentScale.Fit
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = "Instant help. Trusted mechanics.",
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF6B625C)
        )
    }
}