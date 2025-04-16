package com.example.parkingappits.utils

import android.content.Context
import android.util.Log
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

object OpenRouteServiceHelper {
    private const val BASE_URL = "https://api.openrouteservice.org/v2/directions/driving-car"

    fun getDistance(
        context: Context,
        startLat: Double, startLng: Double,
        endLat: Double, endLng: Double,
        onSuccess: (Double) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val apiKey = ApiKeyHelper.getApiKey(context)
        val url = "$BASE_URL?api_key=$apiKey&start=$startLng,$startLat&end=$endLng,$endLat"

        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ORSHelper", "API request failed: ${e.message}")
                onFailure("Lỗi kết nối: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body?.string()?.let { responseBody ->
                        try {
                            val jsonResponse = JSONObject(responseBody)
                            val distance = jsonResponse
                                .getJSONArray("features")
                                .getJSONObject(0)
                                .getJSONObject("properties")
                                .getJSONArray("segments")
                                .getJSONObject(0)
                                .getDouble("distance")

                            onSuccess(distance)
                        } catch (e: Exception) {
                            onFailure("Lỗi phân tích phản hồi: ${e.message}")
                        }
                    }
                } else {
                    onFailure("Lỗi từ API: ${response.message}")
                }
            }
        })
    }
}
