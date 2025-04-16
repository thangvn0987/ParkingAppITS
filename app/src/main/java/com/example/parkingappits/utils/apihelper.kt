package com.example.parkingappits.utils

import android.content.Context
import android.util.Log

object ApiKeyHelper {

    /**
     * Lấy API Key từ AndroidManifest.xml
     * @param context: Context của ứng dụng
     * @return API Key dưới dạng String
     */
    fun getApiKey(context: Context): String {
        return try {
            val ai = context.packageManager.getApplicationInfo(
                context.packageName,
                android.content.pm.PackageManager.GET_META_DATA
            )
            val value = ai.metaData.getString("com.google.android.geo.API_KEY")
            value ?: throw Exception("API Key not found in Manifest")
        } catch (e: Exception) {
            Log.e("ApiKeyHelper", "Error retrieving API Key: ${e.message}")
            throw RuntimeException("Error retrieving API Key: ${e.message}")
        }
    }
}
