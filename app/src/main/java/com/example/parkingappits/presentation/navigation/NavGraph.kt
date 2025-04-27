package com.example.parkingappits.presentation.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.parkingappits.presentation.ui.screen.HomeScreen
import com.example.parkingappits.presentation.ui.screen.ParkingDetailScreen
import com.example.parkingappits.presentation.ui.screen.ProfileScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Profile : Screen("profile")
    @SuppressLint("StateFlowValueCalledInComposition")
    @Composable
    fun NavGraph(navController: NavHostController) {
        NavHost(navController = navController, startDestination = Screen.Home.route) {

            // HomeScreen
            composable(route = Screen.Home.route) {
                HomeScreen(navController = navController)
            }

            // ProfileScreen
            composable(route = Screen.Profile.route) {
                ProfileScreen(navController = navController)
            }

//            ParkinDetailScreen
            composable(
                route = "parking_detail/{parkingId}",
                arguments = listOf(navArgument("parkingId") { type = NavType.StringType })
            ) { backStackEntry ->
                val parkingId = backStackEntry.arguments?.getString("parkingId") ?: ""
                ParkingDetailScreen(navController, parkingId)
            }
        }
    }
}
