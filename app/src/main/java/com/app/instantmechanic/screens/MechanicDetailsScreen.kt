package com.app.instantmechanic.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.domain.model.isOpenNow
import com.app.instantmechanic.MechanicViewModel
import com.app.instantmechanic.utils.formatTimeTo12Hour

@Composable
fun MechanicDetailsScreen(
    mechanicId: String,
    viewModel: MechanicViewModel,
    onBack: () -> Unit,
    onRequestService: () -> Unit
) {
    val mechanic = viewModel.getMechanicById(mechanicId)

    if (mechanic == null) {
        Text("Mechanic not found")
        return
    }

    val isOpen = mechanic.isOpenNow()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFBF3))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        TextButton(
            onClick = onBack
        ) {
            Text(
                text = "← Back",
                color = Color(0xFFFF6B0B)
            )
        }

        Text(
            text = mechanic.name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1C1917)
        )

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
                text = "(${mechanic.reviewCount} reviews)",
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

        Surface(
            shape = RoundedCornerShape(50),
            color = if (isOpen) {
                Color(0xFFEAF7E6)
            } else {
                Color(0xFFFFECEA)
            }
        ) {
            Text(
                text = if (isOpen) {
                    "Open until ${formatTimeTo12Hour(mechanic.closeTime)}"
                } else {
                    "Closed • Opens ${formatTimeTo12Hour(mechanic.openTime)}"
                },
                modifier = Modifier.padding(
                    horizontal = 12.dp,
                    vertical = 5.dp
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

        HorizontalDivider(
            color = Color(0xFFE8E0D9)
        )

        DetailSection(
            title = "Address"
        ) {
            Text(
                text = mechanic.address,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1C1917)
            )
        }

        HorizontalDivider(
            color = Color(0xFFE8E0D9)
        )

        DetailSection(
            title = "Working hours"
        ) {
            Text(
                text = "${formatTimeTo12Hour(mechanic.openTime)} - ${formatTimeTo12Hour(mechanic.closeTime)}",                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1C1917)
            )
        }

        HorizontalDivider(
            color = Color(0xFFE8E0D9)
        )

        DetailSection(
            title = "Phone number"
        ) {
            Text(
                text = mechanic.phoneNumber,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFFF6B0B)
            )
        }

        HorizontalDivider(
            color = Color(0xFFE8E0D9)
        )

        DetailSection(
            title = "Services"
        ) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                mechanic.services.forEach { service ->
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

        Button(
            onClick = onRequestService,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF6B0B)
            )
        ) {
            Text(
                text = "Request Service",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun DetailSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            color = Color(0xFF8B817A)
        )

        content()
    }
}