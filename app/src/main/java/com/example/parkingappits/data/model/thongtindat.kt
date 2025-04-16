package com.example.parkingappits.data.model

data class Reservation(
    val id: String = "",
    val userId: String = "",
    val parkingId: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "pending",
    val slotNumber: Int = 0,
    val vehicleNumber: String = ""
)
