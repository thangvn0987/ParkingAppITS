package com.example.parkingappits.data.repository

import android.util.Log
import com.example.parkingappits.data.model.BaiDo
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ParkingRepository {
    private val db = FirebaseFirestore.getInstance()

    suspend fun loadParkingLotsFromFirebase(): List<BaiDo> {
        return try {
            val snapshot = db.collection("parking_lots").get().await()

            if (snapshot.isEmpty) {
                Log.e("ParkingRepository", "No parking lots found")
                return emptyList()
            } else {
                Log.d("ParkingRepository", "Found ${snapshot.documents.size} parking lots")
            }

            snapshot.documents.mapNotNull { doc ->
                try {
//                    val id = doc.id
                    val id = doc.getString("id") ?:"Unknown"

                    val name = doc.getString("name") ?: "Unknown"

                    // Xử lý available_slots: Chuỗi -> Số nguyên
                    val availableSlots = try {
                        doc.getString("available_slots")?.toInt() ?: 0
                    } catch (e: Exception) {
                        0
                    }

                    // Xử lý latitude: Chuỗi -> Số thực
                    val latitude = try {
                        doc.getString("latitude")?.toDouble() ?: 0.0
                    } catch (e: Exception) {
                        0.0
                    }

                    // Xử lý longitude: Chuỗi -> Số thực
                    val longitude = try {
                        doc.getString("longitude")?.toDouble() ?: 0.0
                    } catch (e: Exception) {
                        0.0
                    }

                    // Đặt log để kiểm tra
                    Log.d("ParkingRepository", "Loaded: $name, Slots: $availableSlots, Lat: $latitude, Lng: $longitude")

                    // Tạo đối tượng BaiDo
                    BaiDo(
                        id = id,
                        name = name,
                        available_Slots = availableSlots,
                        latitude = latitude,
                        longitude = longitude
                    )
                } catch (e: Exception) {
                    Log.e("ParkingRepository", "Error parsing document: ${e.message}")
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("ParkingRepository", "Error loading parking lots: ${e.message}")
            emptyList()
        }
    }
}
