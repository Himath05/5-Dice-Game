package com.example.mobile_application_development_dice.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mobile_application_development_dice.ui.screens.AboutScreen
import com.example.mobile_application_development_dice.ui.screens.GameScreen
import com.example.mobile_application_development_dice.ui.screens.HomeScreen
import com.example.mobile_application_development_dice.viewmodel.GameViewModel

/**
 * Navigation routes for the app
 */
object AppDestinations {
    const val HOME_ROUTE = "home"
    const val GAME_ROUTE = "game"
    const val ABOUT_ROUTE = "about"
}

/**
 * Main navigation component for the app
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val gameViewModel: GameViewModel = viewModel()
    
    val actions = remember(navController) { AppNavigationActions(navController) }
    
    NavHost(
        navController = navController,
        startDestination = AppDestinations.HOME_ROUTE
    ) {
        composable(AppDestinations.HOME_ROUTE) {
            HomeScreen(
                onNewGameClick = { targetScore ->
                    gameViewModel.startNewGame(targetScore)
                    actions.navigateToGame()
                },
                onAboutClick = actions.navigateToAbout
            )
        }
        
        composable(AppDestinations.GAME_ROUTE) {
            GameScreen(
                gameViewModel = gameViewModel,
                onBackClick = actions.navigateToHome
            )
        }
        
        composable(AppDestinations.ABOUT_ROUTE) {
            AboutScreen(
                onBackClick = actions.navigateToHome
            )
        }
    }
}

/**
 * Navigation actions for the app
 */
class AppNavigationActions(private val navController: androidx.navigation.NavController) {
    
    val navigateToHome: () -> Unit = {
        navController.navigate(AppDestinations.HOME_ROUTE) {
            popUpTo(AppDestinations.HOME_ROUTE) {
                inclusive = true
            }
        }
    }
    
    val navigateToGame: () -> Unit = {
        navController.navigate(AppDestinations.GAME_ROUTE)
    }
    
    val navigateToAbout: () -> Unit = {
        navController.navigate(AppDestinations.ABOUT_ROUTE)
    }
} 