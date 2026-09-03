package com.app.instantmechanic.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.domain.model.ServiceRequest
import com.app.instantmechanic.MechanicViewModel
import com.app.instantmechanic.ServiceRequestViewModel
import com.app.instantmechanic.screens.MechanicDetailsScreen
import com.app.instantmechanic.screens.MechanicScreen
import com.app.instantmechanic.screens.RequestServiceScreen

@Composable
fun InstantMechanicNavHost() {
    val navController = rememberNavController()
    val mechanicViewModel: MechanicViewModel = hiltViewModel()
    val serviceRequestViewModel: ServiceRequestViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {

        composable(Routes.HOME) {
            MechanicScreen(
                viewModel = mechanicViewModel,
                onMechanicClick = { mechanicId ->
                    navController.navigate("details/$mechanicId")
                }
            )
        }

        composable(Routes.DETAILS) { backStackEntry ->
            val mechanicId =
                backStackEntry.arguments
                    ?.getString("mechanicId")
                    .orEmpty()

            MechanicDetailsScreen(
                mechanicId = mechanicId,
                viewModel = mechanicViewModel,
                onBack = {
                    navController.popBackStack()
                },
                onRequestService = {
                    navController.navigate("request/$mechanicId")
                }
            )
        }

        composable(Routes.REQUEST) { backStackEntry ->

            val mechanicId = backStackEntry.arguments
                ?.getString("mechanicId")
                .orEmpty()

            val mechanic = mechanicViewModel.getMechanicById(mechanicId)


            if (mechanic != null) {
                RequestServiceScreen(
                    mechanic = mechanic,
                    onBack = {
                        navController.popBackStack()
                    },
                    onSubmit = { name, phone, vehicle, service, problem ->

                        val request = ServiceRequest(
                            mechanicId = mechanic.id,
                            customerName = name,
                            phoneNumber = phone,
                            vehicleNumber = vehicle,
                            selectedService = service,
                            problemDescription = problem
                        )

                        serviceRequestViewModel.submitRequest(
                            request = request,
                            onSuccess = {
                                Log.d("ServiceRequest", "Success callback received")
                            },
                            onError = { error ->
                                Log.e("ServiceRequest", "Error: ${error.message}")
                            }
                        )
                    }
                )
            }
        }
    }
}