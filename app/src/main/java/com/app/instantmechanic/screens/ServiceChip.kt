package com.app.instantmechanic.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CarRepair
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TireRepair
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun ServiceChip(
    service: String
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFFFFF4EB),
        border = BorderStroke(
            width = 1.dp,
            color = Color(0xFFFFA768)
        )
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 6.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = iconForService(service),
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = Color(0xFFFF6B0B)
            )

            Spacer(modifier = Modifier.width(5.dp))

            Text(
                text = service,
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF4A403A),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

private fun iconForService(
    service: String
): ImageVector {
    val normalizedService = service
        .trim()
        .lowercase()

    return when {
        normalizedService.startsWith("ac ") ||
                normalizedService.contains("air conditioning") -> {
            Icons.Default.AcUnit
        }

        normalizedService.contains("brake") -> {
            Icons.Default.CarRepair
        }

        normalizedService.contains("jump start") -> {
            Icons.Default.ElectricBolt
        }

        normalizedService.contains("battery") -> {
            Icons.Default.BatteryChargingFull
        }

        normalizedService.contains("dent") ||
                normalizedService.contains("paint") -> {
            Icons.Default.FormatPaint
        }

        normalizedService.contains("engine") -> {
            Icons.Default.Settings
        }

        normalizedService.contains("alignment") -> {
            Icons.Default.Tune
        }

        normalizedService.contains("tyre") ||
                normalizedService.contains("tire") ||
                normalizedService.contains("puncture") -> {
            Icons.Default.TireRepair
        }

        else -> {
            Icons.Default.Build
        }
    }
}