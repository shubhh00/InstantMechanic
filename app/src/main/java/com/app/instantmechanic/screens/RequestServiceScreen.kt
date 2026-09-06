package com.app.instantmechanic.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.app.domain.model.Mechanic
import com.app.instantmechanic.ServiceRequestUiState

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun RequestServiceScreen(
    mechanic: Mechanic,
    uiState: ServiceRequestUiState,
    onBack: () -> Unit,
    onSubmit: (
        String,
        String,
        String,
        String,
        String
    ) -> Unit,
    onSuccessDismiss: () -> Unit,
    onErrorDismiss: () -> Unit
) {
    var customerName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var vehicleNumber by remember { mutableStateOf("") }
    var selectedService by remember { mutableStateOf("") }
    var problemDescription by remember { mutableStateOf("") }

    var serviceMenuExpanded by remember { mutableStateOf(false) }
    var showErrors by remember { mutableStateOf(false) }

    val isPhoneValid =
        phoneNumber.length == 10 &&
                phoneNumber.all { it.isDigit() }

    val isFormValid =
        customerName.isNotBlank() &&
                isPhoneValid &&
                vehicleNumber.isNotBlank() &&
                selectedService.isNotBlank() &&
                problemDescription.isNotBlank()

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color(0xFFFF6B0B),
        focusedLabelColor = Color(0xFFFF6B0B),
        cursorColor = Color(0xFFFF6B0B),

        unfocusedBorderColor = Color(0xFFD9CEC5),
        unfocusedLabelColor = Color(0xFF746A63),

        focusedTextColor = Color(0xFF1C1917),
        unfocusedTextColor = Color(0xFF1C1917),

        errorBorderColor = Color(0xFFC62828),
        errorLabelColor = Color(0xFFC62828)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFBF3))
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        TextButton(
            onClick = onBack,
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = "← Back",
                color = Color(0xFFFF6B0B)
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Request Service",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1C1917)
            )

            Text(
                text = mechanic.name,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF746A63)
            )
        }

        Spacer(
            modifier = Modifier.height(2.dp)
        )

        OutlinedTextField(
            value = customerName,
            onValueChange = {
                customerName = it
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Customer name")
            },
            singleLine = true,
            isError = showErrors && customerName.isBlank(),
            colors = textFieldColors,
            shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { input ->
                phoneNumber = input
                    .filter(Char::isDigit)
                    .take(10)
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Phone number")
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone
            ),
            isError = showErrors && !isPhoneValid,
            supportingText = {
                if (showErrors && !isPhoneValid) {
                    Text(
                        text = "Enter a valid 10-digit phone number"
                    )
                }
            },
            colors = textFieldColors,
            shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
            value = vehicleNumber,
            onValueChange = {
                vehicleNumber = it.uppercase()
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Vehicle number")
            },
            singleLine = true,
            isError = showErrors && vehicleNumber.isBlank(),
            colors = textFieldColors,
            shape = RoundedCornerShape(12.dp)
        )

        ExposedDropdownMenuBox(
            expanded = serviceMenuExpanded,
            onExpandedChange = {
                serviceMenuExpanded = !serviceMenuExpanded
            },
        ) {
            OutlinedTextField(
                value = selectedService,
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                label = {
                    Text("Service needed")
                },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = serviceMenuExpanded
                    )
                },
                isError = showErrors && selectedService.isBlank(),
                colors = textFieldColors,
                shape = RoundedCornerShape(12.dp)
            )

            ExposedDropdownMenu(
                expanded = serviceMenuExpanded,
                onDismissRequest = {
                    serviceMenuExpanded = false
                }
            ) {
                mechanic.services
                    .sortedBy { it.lowercase() }
                    .forEach { service ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = service,
                                    color = Color(0xFF1C1917)
                                )
                            },
                            onClick = {
                                selectedService = service
                                serviceMenuExpanded = false
                            },
                            colors = MenuDefaults.itemColors(
                                textColor = Color(0xFF1C1917)
                            ),
                            modifier = Modifier.background(
                                Color(0xFFFFFBF3)
                            )
                        )
                    }
            }
        }

        OutlinedTextField(
            value = problemDescription,
            onValueChange = {
                problemDescription = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            label = {
                Text("Problem description")
            },
            minLines = 4,
            isError = showErrors && problemDescription.isBlank(),
            colors = textFieldColors,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Button(
            onClick = {
                showErrors = true

                if (
                    isFormValid &&
                    uiState !is ServiceRequestUiState.Loading
                ) {
                    onSubmit(
                        customerName,
                        phoneNumber,
                        vehicleNumber,
                        selectedService,
                        problemDescription
                    )
                }
            },
            enabled = uiState !is ServiceRequestUiState.Loading,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF6B0B),
                contentColor = Color.White,
                disabledContainerColor = Color(0xFFFFB27A)
            )
        ) {
            if (uiState is ServiceRequestUiState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    strokeWidth = 2.dp,
                    color = Color.White
                )
            } else {
                Text(
                    text = "Submit Request",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    val successState = uiState as? ServiceRequestUiState.Success

    if (successState != null) {
        AlertDialog(
            onDismissRequest = onSuccessDismiss,
            title = {
                Text(
                    text = if (successState.queuedForSync) {
                        "Request saved"
                    } else {
                        "Request submitted"
                    }
                )
            },
            text = {
                Text(
                    text = if (successState.queuedForSync) {
                        "You're offline. Your request is saved and will be sent automatically when you're back online."
                    } else {
                        "Your service request has been submitted successfully."
                    }
                )
            },
            confirmButton = {
                Button(onClick = onSuccessDismiss) {
                    Text("OK")
                }
            }
        )
    }

    if (uiState is ServiceRequestUiState.Error) {
        AlertDialog(
            onDismissRequest = onErrorDismiss,
            containerColor = Color(0xFFFFFBF3),
            title = {
                Text(
                    text = "Submission failed",
                    color = Color(0xFF1C1917),
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = uiState.message,
                    color = Color(0xFF746A63)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = onErrorDismiss
                ) {
                    Text(
                        text = "OK",
                        color = Color(0xFFFF6B0B)
                    )
                }
            }
        )
    }
}