package com.suraj.weathersnap.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface AppRoute : NavKey

@Serializable
object HomeScreen : AppRoute
@Serializable
object CreateReportScreen : AppRoute
@Serializable
object SavedReportScreen : AppRoute

@Serializable
object CameraScreen: AppRoute

