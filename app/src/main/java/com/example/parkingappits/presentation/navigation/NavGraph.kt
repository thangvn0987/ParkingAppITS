package com.example.parkingappits.presentation.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
//import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.parkingappits.data.model.BaiDo
import com.example.parkingappits.presentation.ui.screen.HomeScreen

import com.example.parkingappits.presentation.ui.screen.ParkingDetailScreen
import com.example.parkingappits.presentation.ui.screen.ProfileScreen
import com.example.parkingappits.presentation.viewmodel.HomeViewModel

import com.google.android.gms.maps.model.LatLng
//import java.lang.reflect.Modifier
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize


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

            //man hinh chi tiet
//            composable("parking_detail/{parkingId}") { backStackEntry ->
//                val parkingId = backStackEntry.arguments?.getString("parkingId") ?: ""
//                ParkingDetailScreen(parkingId)
//            }
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
