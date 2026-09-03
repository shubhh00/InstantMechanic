package com.app.instantmechanic.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.app.domain.model.Mechanic

@Composable
fun RequestServiceScreen(
    mechanic: Mechanic,
    onBack: () -> Unit,
    onSubmit: (
        customerName: String,
        phoneNumber: String,
        vehicleNumber: String,
        selectedService: String,
        problemDescription: String
    ) -> Unit
) {
    var customerName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var vehicleNumber by remember { mutableStateOf("") }
    var selectedService by remember { mutableStateOf("") }
    var problemDescription by remember { mutableStateOf("") }

    var serviceMenuExpanded by remember { mutableStateOf(false) }
    var showErrors by remember { mutableStateOf(false) }

    val isPhoneValid =
        phoneNumber.length == 10 && phoneNumber.all { it.isDigit() }

    val isFormValid =
        customerName.isNotBlank() &&
                isPhoneValid &&
                vehicleNumber.isNotBlank() &&
                selectedService.isNotBlank() &&
                problemDescription.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFBF3))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        TextButton(
            onClick = onBack
        ) {
            Text("← Back")
        }

        Text(
            text = "Request Service",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF171717)
        )

        Text(
            text = mechanic.name,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF6B625C)
        )

        OutlinedTextField(
            value = customerName,
            onValueChange = { customerName = it },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Customer name")
            },
            singleLine = true,
            isError = showErrors && customerName.isBlank()
        )

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = {
                if (it.length <= 10) {
                    phoneNumber = it.filter(Char::isDigit)
                }
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
                    Text("Enter a valid 10-digit phone number")
                }
            }
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
            isError = showErrors && vehicleNumber.isBlank()
        )

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedService,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Select service")
                },
                readOnly = true,
                trailingIcon = {
                    IconButton(
                        onClick = {
                            serviceMenuExpanded = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Select service"
                        )
                    }
                },
                isError = showErrors && selectedService.isBlank()
            )

            DropdownMenu(
                expanded = serviceMenuExpanded,
                onDismissRequest = {
                    serviceMenuExpanded = false
                }
            ) {
                mechanic.services.forEach { service ->
                    DropdownMenuItem(
                        text = {
                            Text(service)
                        },
                        onClick = {
                            selectedService = service
                            serviceMenuExpanded = false
                        }
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
            isError = showErrors && problemDescription.isBlank()
        )

        Button(
            onClick = {
                showErrors = true

                if (isFormValid) {
                    onSubmit(
                        customerName,
                        phoneNumber,
                        vehicleNumber,
                        selectedService,
                        problemDescription
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF6B0B)
            )
        ) {
            Text(
                text = "Submit Request",
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}