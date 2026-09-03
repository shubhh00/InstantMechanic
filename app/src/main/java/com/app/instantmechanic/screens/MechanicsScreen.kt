package com.app.instantmechanic.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.instantmechanic.MechanicViewModel

@Composable
fun MechanicScreen(
    modifier: Modifier = Modifier,
    viewModel: MechanicViewModel,
    onMechanicClick: (String) -> Unit
) {
    val mechanics by viewModel.mechanics.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFFFBF3)),
        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 20.dp
        ),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Instant Mechanic",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF171717)
                )

                Text(
                    text = "Find trusted garages around you",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF6B625C)
                )
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )
        }

        items(
            items = mechanics,
            key = { it.id }
        ) { mechanic ->
            MechanicCard(
                mechanic = mechanic,
                onClick = {
                    onMechanicClick(mechanic.id)
                }
            )
        }
    }
}