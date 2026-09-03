package com.app.instantmechanic.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
            width = 1.dp,
            color = Color(0xFFE8E2DC)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Text(
                text = mechanic.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF171717)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {

                Text(
                    text = "★",
                    color = Color(0xFFFF6B0B)
                )

                Text(
                    text = "${mechanic.rating}",
                    color = Color(0xFF171717),
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "(${mechanic.reviewCount})",
                    color = Color(0xFF6B625C)
                )

                Text(
                    text = "•",
                    color = Color(0xFF9A928C)
                )

                Text(
                    text = "${mechanic.distanceKm} km",
                    color = Color(0xFF171717)
                )
            }

            Text(
                text = mechanic.location,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF6B625C)
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                mechanic.services.forEach { service ->

                    AssistChip(
                        onClick = {},
                        label = {
                            Text(
                                text = service,
                                color = Color(0xFF5A4030)
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = Color(0xFFFFF3E8)
                        ),
                        border = AssistChipDefaults.assistChipBorder(
                            enabled = true,
                            borderColor = Color(0xFFFFB27A)
                        )
                    )
                }
            }

            Text(
                text = if (isOpen) "Open" else "Closed",
                color = if (isOpen) {
                    Color(0xFF2E7D32)
                } else {
                    Color(0xFFC62828)
                },
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}