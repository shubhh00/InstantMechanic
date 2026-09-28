package com.app.instantmechanic.navigation

import android.net.Uri
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
import android.os.Bundle
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.app.instantmechanic.BuildConfig
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

@Composable
fun InstantMechanicNavHost(
    notificationMechanicId: String?,
    onNotificationHandled: () -> Unit
) {
    val navController = rememberNavController()
    val mechanicViewModel: MechanicViewModel = hiltViewModel()
    val context = LocalContext.current
    val analytics = remember(context) {
        FirebaseAnalytics.getInstance(context.applicationContext)
    }

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
            val remoteConfig = remember { FirebaseRemoteConfig.getInstance() }
            var videoConsultationEnabled by remember { mutableStateOf(true) }
            LaunchedEffect(remoteConfig) {
                val settings = FirebaseRemoteConfigSettings.Builder()
                    .setMinimumFetchIntervalInSeconds(
                        if (BuildConfig.DEBUG) 0L else 3600L
                    )
                    .build()

                remoteConfig.setConfigSettingsAsync(settings)
                    .addOnCompleteListener {
                        remoteConfig.setDefaultsAsync(
                            mapOf("video_consultation_enabled" to true)
                        ).addOnCompleteListener { defaultsTask ->
                            if (defaultsTask.isSuccessful) {
                                videoConsultationEnabled = remoteConfig.getBoolean(
                                    "video_consultation_enabled"
                                )
                            }

                            remoteConfig.fetchAndActivate()
                                .addOnCompleteListener { fetchTask ->
                                    if (fetchTask.isSuccessful) {
                                        videoConsultationEnabled =
                                            remoteConfig.getBoolean(
                                                "video_consultation_enabled"
                                            )
                                    }
                                }
                        }
                    }
            }
            MechanicScreen(
                viewModel = mechanicViewModel,
                videoConsultationEnabled = videoConsultationEnabled,
                onMechanicClick = { mechanicId ->
                    analytics.logEvent(
                        "mechanic_opened",
                        Bundle().apply {
                            putString("mechanic_id", mechanicId)
                        }
                    )
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

    LaunchedEffect(notificationMechanicId) {
        val mechanicId = notificationMechanicId ?: return@LaunchedEffect

        if (navController.currentDestination?.route == Routes.SPLASH) {
            navController.navigate(Routes.HOME) {
                popUpTo(Routes.SPLASH) { inclusive = true }
                launchSingleTop = true
            }
        }

        navController.navigate("details/${Uri.encode(mechanicId)}") {
            launchSingleTop = true
        }

        onNotificationHandled()
    }
}