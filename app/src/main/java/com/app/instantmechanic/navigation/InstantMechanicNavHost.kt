package com.app.instantmechanic.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.app.instantmechanic.screens.SplashScreen
import com.app.instantmechanic.video.VideoCallViewModel
import com.app.instantmechanic.video.VideoConsultationScreen

@Composable
fun InstantMechanicNavHost() {
    val navController = rememberNavController()
    val mechanicViewModel: MechanicViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {

        composable(Routes.SPLASH) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.SPLASH) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Routes.HOME) {
            MechanicScreen(
                viewModel = mechanicViewModel,
                onMechanicClick = { mechanicId ->
                    navController.navigate("details/$mechanicId")
                },
                onVideoConsultationClick = {
                    navController.navigate(
                        Routes.VIDEO_CONSULTATION
                    ) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Routes.VIDEO_CONSULTATION) {
            val videoCallViewModel: VideoCallViewModel = hiltViewModel()

            VideoConsultationScreen(
                viewModel = videoCallViewModel,
                onCallFinished = {
                    navController.popBackStack()
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
            val serviceRequestViewModel: ServiceRequestViewModel = hiltViewModel()
            val uiState by serviceRequestViewModel.uiState.collectAsState()

            val mechanicId = backStackEntry.arguments
                ?.getString("mechanicId")
                .orEmpty()

            val mechanic = mechanicViewModel.getMechanicById(mechanicId)


            if (mechanic != null) {
                RequestServiceScreen(
                    mechanic = mechanic,
                    uiState = uiState,
                    onBack = {
                        navController.popBackStack()
                    },
                    onSubmit = { name, phone, vehicle, service, problem ->
                        serviceRequestViewModel.submitRequest(
                            ServiceRequest(
                                mechanicId = mechanic.id,
                                customerName = name,
                                phoneNumber = phone,
                                vehicleNumber = vehicle,
                                selectedService = service,
                                problemDescription = problem
                            )
                        )
                    },
                    onSuccessDismiss = {
                        serviceRequestViewModel.resetState()

                        navController.popBackStack(
                            route = Routes.HOME,
                            inclusive = false
                        )
                    },
                    onErrorDismiss = {
                        serviceRequestViewModel.resetState()
                    }
                )
            }
        }
    }
}