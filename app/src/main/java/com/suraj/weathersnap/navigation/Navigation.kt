package com.suraj.weathersnap.navigation

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.suraj.weathersnap.Presentation.CameraScreen
import com.suraj.weathersnap.Presentation.CreateReportScreen
import com.suraj.weathersnap.Presentation.HomeScreen
import com.suraj.weathersnap.Presentation.SavedReportsScreen
import com.suraj.weathersnap.Presentation.WeatherViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun WeatherSnapNavigation(
    weatherViewModel: WeatherViewModel = koinViewModel(),
    padding : PaddingValues
){
    val backStack = rememberNavBackStack(HomeScreen)

    val onBackPressed = {
        if (backStack.size >1 ){
            backStack.removeLastOrNull()
        }
    }

    NavDisplay(
        backStack = backStack,
        onBack = onBackPressed,
        transitionSpec = {
            ContentTransform(
                targetContentEnter = scaleIn(
                    animationSpec = tween(400, easing = FastOutSlowInEasing),
                    initialScale = 0.75f
                ) + fadeIn(animationSpec = tween(300)),

                initialContentExit = scaleOut(
                    animationSpec = tween(400, easing = FastOutSlowInEasing),
                    targetScale = 1.1f
                ) + fadeOut(animationSpec = tween(200))
            )
        },
        popTransitionSpec = {
            ContentTransform(
                targetContentEnter = scaleIn(
                    animationSpec = tween(400, easing = FastOutSlowInEasing),
                    initialScale = 1.1f
                ) + fadeIn(animationSpec = tween(300)),

                initialContentExit = scaleOut(
                    animationSpec = tween(400, easing = FastOutSlowInEasing),
                    targetScale = 0.75f
                ) + fadeOut(animationSpec = tween(200))
            )
        },
        entryProvider = entryProvider{
            entry<HomeScreen>{
                HomeScreen(
                    onNavigateToReports = {
                        backStack.add(SavedReportScreen)
                    },
                    onNavigateToCreateReport = {
                        backStack.add(CreateReportScreen)
                    },
                    padding = padding,
                )
            }

            entry <SavedReportScreen>{
                SavedReportsScreen(
                    onBack = onBackPressed,
                    padding = padding
                )
            }

            entry <CreateReportScreen>{
                CreateReportScreen(
                    onBack = {
                        onBackPressed()
                        weatherViewModel.clearReportState()
                    },
                    onOpenCamera = { backStack.add(CameraScreen)},
                    onSaved = { backStack.add(SavedReportScreen)},
                    padding = padding
                )
            }

            entry<CameraScreen> {
                CameraScreen(
                    onPhotoCaptured = { imagePath, originalKb,compressedKb ->
                        weatherViewModel.updatePhotoProperties(
                            imagePath,
                            originalKb,
                            compressedKb
                        )
                    },
                    onClose = onBackPressed
                )
            }
        }
    )
}