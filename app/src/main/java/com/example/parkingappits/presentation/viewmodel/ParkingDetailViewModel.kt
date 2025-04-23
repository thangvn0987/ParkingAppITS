package com.example.parkingappits.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkingappits.data.model.ParkingDetail
import com.example.parkingappits.data.model.Reservation
import com.example.parkingappits.data.model.User
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ParkingDetailViewModel : ViewModel() {
    private val _parkingDetail = MutableStateFlow<ParkingDetail?>(null)
    val parkingDetail = _parkingDetail.asStateFlow()

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> get() = _user

    private val _vehicleNumber = MutableStateFlow("")
    val vehicleNumber: StateFlow<String> get() = _vehicleNumber


    fun checkAndConfirmReservationWithSlotUpdate(
        userId: String,
        parkingId: String,
        slotNumber: Int,
        vehicleNumber: String,
        onAlreadyReserved: () -> Unit,
        onReserved: () -> Unit,
        onError: (String) -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()

        // B1: Kiểm tra xem đã có đặt chỗ chưa
        db.collection("reservations")
            .whereEqualTo("userId", userId)
            .whereEqualTo("vehicleNumber", vehicleNumber)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    onAlreadyReserved()
                } else {
                    // B2: Tiến hành transaction để cập nhật availableSlots và tạo đặt chỗ
                    db.runTransaction { transaction ->

                        val parkingRef = db.collection("parkings").document(parkingId)
                        val parkingSnapshot = transaction.get(parkingRef)

                        val currentSlots = parkingSnapshot.getLong("availableSlots") ?: 0L

                        if (currentSlots <= 0) {
                            throw Exception("Bãi đỗ đã hết chỗ.")
                        }

                        // Giảm 1 chỗ trống
                        transaction.update(parkingRef, "availableSlots", currentSlots - 1)

                        // Tạo đặt chỗ mới
                        val reservationId = db.collection("reservations").document().id
                        val reservation = Reservation(
                            id = reservationId,
                            userId = userId,
                            parkingId = parkingId,
                            timestamp = System.currentTimeMillis(),
                            status = "confirmed",
                            slotNumber = slotNumber,
                            vehicleNumber = vehicleNumber
                        )

                        val reservationRef = db.collection("reservations").document(reservationId)
                        transaction.set(reservationRef, reservation)

                        // Trả về nếu thành công
                        null
                    }.addOnSuccessListener {
                        onReserved()
                    }.addOnFailureListener { e ->
                        onError(e.message ?: "Không thể đặt chỗ.")
                    }
                }
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Không thể kiểm tra đặt chỗ.")
            }
    }

    fun loadParkingDetail(parkingId: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("parkings").document(parkingId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val parking = document.toObject(ParkingDetail::class.java)
                    if (parking != null) {  // Kiểm tra nếu không null
                        viewModelScope.launch {
                            _parkingDetail.value = parking
                        }
                    } else {
                        println("Error: Parking detail is null.")
                    }
                } else {
                    println("Error: Document does not exist.")
                }
            }
            .addOnFailureListener { e ->
                println("Error fetching parking detail: ${e.message}")
            }

    }
}
