package com.app.instantmechanic.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.domain.model.Mechanic
import com.app.domain.model.isOpenNow

@Composable
fun MechanicCard(
    mechanic: Mechanic,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isOpen = mechanic.isOpenNow()

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(
            1.dp,
            Color(0xFFE7DDD3)
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = mechanic.name,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1C1917)
                )

                Surface(
                    shape = RoundedCornerShape(50),
                    color = if (isOpen) {
                        Color(0xFFEAF7E6)
                    } else {
                        Color(0xFFFFECEA)
                    }
                ) {
                    Text(
                        text = if (isOpen) "Open" else "Closed",
                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 4.dp
                        ),
                        color = if (isOpen) {
                            Color(0xFF2E7D32)
                        } else {
                            Color(0xFFC62828)
                        },
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = "★",
                    color = Color(0xFFFF6B0B)
                )

                Text(
                    text = "${mechanic.rating}",
                    color = Color(0xFF1C1917),
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "(${mechanic.reviewCount})",
                    color = Color(0xFF746A63)
                )

                Text(
                    text = "•",
                    color = Color(0xFFAAA09A)
                )

                Text(
                    text = "${mechanic.distanceKm} km",
                    color = Color(0xFF1C1917)
                )
            }

            Text(
                text = mechanic.location,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF746A63)
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                mechanic.services.sorted().forEach { service ->
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFFFF4EB),
                        border = BorderStroke(
                            1.dp,
                            Color(0xFFFFA768)
                        )
                    ) {
                        Text(
                            text = service,
                            modifier = Modifier.padding(
                                horizontal = 12.dp,
                                vertical = 7.dp
                            ),
                            color = Color(0xFF5A3420),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        }
    }
}