package com.example.parkingappits

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.rememberNavController
import com.example.parkingappits.presentation.navigation.Screen.Home.NavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        uploadAllParkings()
//            TestApiScreen()
        setContent {
            requestLocationPermission(this)
            val navController = rememberNavController()
            NavGraph(navController = navController)
        }
    }
}
private fun requestLocationPermission(context: Context) {
    val permission = android.Manifest.permission.ACCESS_FINE_LOCATION
    if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(permission),
            101
        )
    }
}
