package com.example.parkingappits

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.rememberNavController
import com.example.parkingappits.presentation.navigation.Screen.Home.NavGraph
import com.example.parkingappits.utils.ApiKeyHelper
import com.example.parkingappits.utils.uploadAllParkings
//import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

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



    /*private fun testApiKey(callback: (String) -> Unit) {
        val apiKey = ApiKeyHelper.getApiKey(this)
        val url = "https://api.openrouteservice.org/v2/directions/driving-car?" +
                "api_key=$apiKey&start=106.687360,10.822238&end=106.721630,10.794182"

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val client = OkHttpClient()
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    Log.d("API Response", responseBody ?: "No response")

                    // Parse JSON response
                    val distance = parseDistanceFromJson(responseBody ?: "")
                    callback("Khoảng cách: $distance km")
                } else {
                    Log.e("API Error", "Request failed: ${response.message}")
                    callback("Lỗi: ${response.message}")
                }
            } catch (e: Exception) {
                Log.e("API Error", e.message ?: "Unknown error")
                callback("Lỗi: ${e.message}")
            }
        }
    }

    private fun parseDistanceFromJson(jsonResponse: String): Double {
        return try {
            val jsonObject = JSONObject(jsonResponse)
            val distance = jsonObject
                .getJSONArray("features")
                .getJSONObject(0)
                .getJSONObject("properties")
                .getJSONArray("segments")
                .getJSONObject(0)
                .getDouble("distance")

            distance / 1000  // Chuyển đổi từ mét sang km
        } catch (e: Exception) {
            Log.e("JSON Error", "Error parsing distance: ${e.message}")
            -1.0
        }
    }

    @Composable
    fun TestApiScreen() {
        var result by remember { mutableStateOf("Kết quả sẽ hiển thị ở đây") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                testApiKey { response ->
                    result = response
                }
            }) {
                Text("Test API Key")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = result)
        }
    }*/

