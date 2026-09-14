package com.app.instantmechanic.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun MechanicHomeHeader(
    mechanicCount: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        border = BorderStroke(
            width = 1.dp,
            color = Color(0xFFE8DED6)
        ),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 18.dp,
                vertical = 16.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = Color(0xFFFFE8D7)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color(0xFFFF6B0B),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement =
                    Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "Near You",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1C1917)
                )

                Text(
                    text = "Trusted garages around you",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF746A63)
                )
            }

            GarageCountBadge(
                mechanicCount = mechanicCount
            )
        }
    }
}

@Composable
private fun GarageCountBadge(
    mechanicCount: Int
) {
    val label = if (mechanicCount == 1) {
        "1 garage"
    } else {
        "$mechanicCount garages"
    }

    Surface(
        shape = RoundedCornerShape(50),
        color = Color(0xFFFFF0E6)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 6.dp
            ),
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFFB94300),
            fontWeight = FontWeight.SemiBold
        )
    }
}