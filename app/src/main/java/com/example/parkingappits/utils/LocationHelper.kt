package com.example.parkingappits.utils

import android.content.Context
import android.location.Location
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.tasks.await

object LocationHelper {

    /**
     * Lấy vị trí thiết bị hiện tại
     */
    suspend fun getDeviceLocation(context: Context): Location? {
        return try {
            val fusedLocationClient: FusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(context)

            // Kiểm tra quyền vị trí
            if (!checkLocationPermission(context)) {
                Log.e("LocationHelper", "Thiếu quyền truy cập vị trí")
                return null
            }

            val location = fusedLocationClient.lastLocation.await()

            if (location != null) {
                Log.d("LocationHelper", "Vị trí hiện tại: ${location.latitude}, ${location.longitude}")
                location
            } else {
                Log.e("LocationHelper", "Không lấy được vị trí, thử lại...")
                null
            }
        } catch (e: Exception) {
            Log.e("LocationHelper", "Lỗi lấy vị trí: ${e.message}")
            null
        }
    }

    /**
     * Kiểm tra quyền vị trí
     */
    fun checkLocationPermission(context: Context): Boolean {
        val permission = android.Manifest.permission.ACCESS_FINE_LOCATION
        return androidx.core.content.ContextCompat.checkSelfPermission(context, permission) ==
                android.content.pm.PackageManager.PERMISSION_GRANTED
    }
}
