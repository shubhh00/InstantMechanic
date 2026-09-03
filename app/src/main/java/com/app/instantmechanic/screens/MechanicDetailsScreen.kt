package com.app.instantmechanic.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.instantmechanic.MechanicViewModel

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFBF3))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        TextButton(
            onClick = onBack
        ) {
            Text("← Back")
        }

        Text(
            text = mechanic.name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF171717)
        )

        Text(
            text = "★ ${mechanic.rating} (${mechanic.reviewCount})",
            color = Color(0xFF171717)
        )

        Text(
            text = mechanic.address,
            color = Color(0xFF6B625C)
        )

        Text(
            text = "${mechanic.openTime} - ${mechanic.closeTime}",
            color = Color(0xFF171717)
        )

        Text(
            text = mechanic.phoneNumber,
            color = Color(0xFF171717)
        )

        Text(
            text = "Services",
            fontWeight = FontWeight.SemiBold
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            mechanic.services.forEach { service ->
                AssistChip(
                    onClick = {},
                    label = {
                        Text(service)
                    }
                )
            }
        }

        Button(
            onClick = onRequestService,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF6B0B)
            )
        ) {
            Text("Request Service")
        }
    }
}