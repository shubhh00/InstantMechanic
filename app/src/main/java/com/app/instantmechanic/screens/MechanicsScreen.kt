package com.app.instantmechanic.screens

import androidx.activity.compose.ReportDrawnWhen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.app.instantmechanic.MechanicListFilter
import com.app.instantmechanic.MechanicViewModel

@Composable
fun MechanicScreen(
    modifier: Modifier = Modifier,
    viewModel: MechanicViewModel,
    onMechanicClick: (String) -> Unit,
    onVideoConsultationClick: () -> Unit,
    videoConsultationEnabled: Boolean,
) {
    val state by viewModel.uiState.collectAsState()

    ReportDrawnWhen {
        !state.isInitialLoading
    }

    when {
        state.isInitialLoading && state.mechanics.isEmpty() -> {
            InitialLoadingContent(modifier)
        }

        state.errorMessage != null && state.mechanics.isEmpty() -> {
            InitialErrorContent(
                modifier = modifier,
                message = state.errorMessage.orEmpty(),
                onRetry = viewModel::refreshMechanics
            )
        }

        else -> {
            PullToRefreshBox(
                isRefreshing = state.isRefreshing,
                onRefresh = viewModel::refreshMechanics,
                modifier = modifier
                    .fillMaxSize()
                    .background(Color(0xFFFFFBF3))
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        horizontal = 16.dp,
                        vertical = 20.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        MechanicHomeHeader(
                            mechanicCount = state.mechanics.size,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }
                    if (videoConsultationEnabled) {
                        item {
                            VideoConsultationCard(
                                onStartConsultation =
                                    onVideoConsultationClick
                            )
                        }
                    }


                    item {
                        MechanicDiscoveryControls(
                            query = state.searchQuery,
                            selectedFilter = state.selectedFilter,
                            onQueryChanged =
                                viewModel::onSearchQueryChanged,
                            onClearSearch = viewModel::clearSearch,
                            onFilterSelected =
                                viewModel::onFilterSelected
                        )
                    }

                    state.errorMessage?.let { message ->
                        item {
                            OfflineBanner(
                                message = message,
                                onRetry = viewModel::refreshMechanics
                            )
                        }
                    }

                    if (state.visibleMechanics.isEmpty()) {
                        item {
                            NoMatchingMechanicsContent(
                                onShowNearby = viewModel::showNearbyMechanics
                            )
                        }
                    }

                    items(
                        items = state.visibleMechanics,
                        key = { mechanic -> mechanic.id }
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
        }
    }
}

@Composable
private fun InitialLoadingContent(
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFFFBF3)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = Color(0xFFFF6B0B)
        )
    }
}

@Composable
private fun InitialErrorContent(
    modifier: Modifier,
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFFFBF3))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = message,
            color = Color(0xFF746A63)
        )

        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF6B0B)
            )
        ) {
            Text("Retry")
        }
    }
}

@Composable
private fun OfflineBanner(
    message: String,
    onRetry: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFFFF0E6)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = message,
                color = Color(0xFF746A63)
            )

            TextButton(
                onClick = onRetry
            ) {
                Text(
                    text = "Try again",
                    color = Color(0xFFFF6B0B)
                )
            }
        }
    }
}

@Composable
private fun MechanicDiscoveryControls(
    query: String,
    selectedFilter: MechanicListFilter,
    onQueryChanged: (String) -> Unit,
    onClearSearch: () -> Unit,
    onFilterSelected: (MechanicListFilter) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChanged,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text("Search mechanics or services")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(
                        onClick = onClearSearch
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear search"
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFF6B0B),
                cursorColor = Color(0xFFFF6B0B)
            )
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MechanicFilterChip(
                label = "Nearby",
                selected = selectedFilter ==
                        MechanicListFilter.NEARBY,
                icon = {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null
                    )
                },
                onClick = {
                    onFilterSelected(
                        MechanicListFilter.NEARBY
                    )
                }
            )

            MechanicFilterChip(
                label = "Open Now",
                selected = selectedFilter ==
                        MechanicListFilter.OPEN_NOW,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null
                    )
                },
                onClick = {
                    onFilterSelected(
                        MechanicListFilter.OPEN_NOW
                    )
                }
            )
        }
    }
}

@Composable
private fun MechanicFilterChip(
    label: String,
    selected: Boolean,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(label)
        },
        leadingIcon = icon,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Color(0xFFFFE2CF),
            selectedLabelColor = Color(0xFFB94300),
            selectedLeadingIconColor = Color(0xFFFF6B0B)
        )
    )
}

@Composable
private fun NoMatchingMechanicsContent(
    onShowNearby: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No matching mechanics found",
            color = Color(0xFF746A63)
        )

        TextButton(
            onClick = onShowNearby
        ) {
            Text(
                text = "Show nearby mechanics",
                color = Color(0xFFFF6B0B)
            )
        }
    }
}