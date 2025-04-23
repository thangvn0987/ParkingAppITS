package com.example.parkingappits.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkingappits.data.model.BaiDo
import com.example.parkingappits.data.model.Reservation
import com.example.parkingappits.data.model.User
import com.example.parkingappits.data.repository.ParkingRepository
import com.example.parkingappits.utils.LocationHelper
import com.example.parkingappits.utils.OpenRouteServiceHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ParkingRepository()
    private val _parkingLots = MutableStateFlow<List<BaiDo>>(emptyList())
    val parkingLots: StateFlow<List<BaiDo>> get() = _parkingLots

    private val _currentLocation = MutableStateFlow<Location?>(null)
    val currentLocation: StateFlow<Location?> get() = _currentLocation

    val isRefreshing = MutableStateFlow(false)

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> get() = _user



    private val _vehicleNumber = MutableStateFlow(getCurrentUserVehicleNumber(getApplication<Application>().applicationContext))
    val vehicleNumber: StateFlow<String> get() = _vehicleNumber

    fun getLatLngByParkingId(parkingId: String): Pair<Double, Double>? {
//        onRefresh()
        return _parkingLots.value.find { it.id == parkingId }
            ?.let { Pair(it.latitude, it.longitude) }
    }



    fun getCurrentUserVehicleNumber(context: Context): String {
        val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPref.getString("biensoxe", "") ?: ""
    }

    private val _currentReservation = MutableStateFlow<Reservation?>(null)
    val currentReservation: StateFlow<Reservation?> = _currentReservation


    fun formatTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }



    fun loadUserReservation() {
        Log.d("ReservationCheck", "da vao ham load reservation ")
        val userId = getCurrentUserId()

        if (userId.isNullOrBlank() || vehicleNumber.value.isNullOrBlank()) {
            // Không đăng nhập hoặc chưa có biển số
            _currentReservation.value = null
            Log.d("ReservationCheck", "userid boi bien so xe co van de")
            return

        }

        FirebaseFirestore.getInstance()
            .collection("reservations")
            .whereEqualTo("userId", userId)
            .whereEqualTo("vehicleNumber", vehicleNumber.value)
            .get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.isEmpty) {
                    val doc = snapshot.documents.firstOrNull()
                    val reservation = doc?.toObject(Reservation::class.java)
                    _currentReservation.value = reservation
                    if (_currentReservation.value == null) {
                        Log.d("ReservationCheck", "reservation is null")
                    }

                } else {
                    Log.d("ReservationCheck", "1_currentReservation is null")
                    _currentReservation.value = null
                }
            }
            .addOnFailureListener {
                Log.d("ReservationCheck", "2_currentReservation is null")
                _currentReservation.value = null
            }
    }
    fun getCurrentUserId(): String {
        return FirebaseAuth.getInstance().currentUser?.uid ?: ""
    }

    fun getParkingNameById(id: String): String {
        return _parkingLots.value.find { it.id == id }?.name ?: "Không rõ"
    }

    fun cancelReservation(onSuccess: () -> Unit, onFailure: () -> Unit) {
        val reservation = _currentReservation.value ?: return

        FirebaseFirestore.getInstance()
            .collection("reservations")
            .document(reservation.id)
            .delete()
            .addOnSuccessListener {
                // Sau khi xóa → cộng lại chỗ trống
                updateSlotCount(parkingId = reservation.parkingId, increase = true)
                _currentReservation.value = null
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailure()
            }
    }
    private fun updateSlotCount(parkingId: String, increase: Boolean) {
        val db = FirebaseFirestore.getInstance()
        val ref = db.collection("parkings").document(parkingId)

        db.runTransaction { transaction ->
            val snapshot = transaction.get(ref)
            val current = snapshot.getLong("availableSlots") ?: 0
            val newCount = if (increase) current + 1 else maxOf(0, current - 1)
            transaction.update(ref, "availableSlots", newCount)
        }
    }

    fun isUserLoggedIn(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }

    init {
        if (isUserLoggedIn()) {
            getCurrentLocation()
        }
    }

    private fun getCurrentLocation() {
        viewModelScope.launch {
            try {
                val context = getApplication<Application>().applicationContext
                if (!LocationHelper.checkLocationPermission(context)) {
                    Log.e("HomeViewModel", "Không có quyền vị trí")
                    _currentLocation.value = null
                    return@launch
                }

                val location = LocationHelper.getDeviceLocation(context)
                if (location != null) {
                    Log.d("HomeViewModel", "Vị trí hiện tại: ${location.latitude}, ${location.longitude}")
                    _currentLocation.value = location
                    refreshParkingLots(location)
                } else {
                    Log.e("HomeViewModel", "Không lấy được vị trí")
                    _currentLocation.value = null
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Lỗi khi lấy vị trí: ${e.message}")
                _currentLocation.value = null
            }
        }
    }

    private fun refreshParkingLots(location: Location) {
        viewModelScope.launch {
            isRefreshing.value = true
            try {
                val lots = repository.loadParkingLotsFromFirebase()
                val sortedLots = lots.map { baiDo ->
                    val distance = getDistanceToParkingLot(location, baiDo)
                    Pair(baiDo, distance)
                }.sortedBy { it.second }
                    .map { it.first }

                _parkingLots.value = sortedLots
                Log.d("HomeViewModel", "Danh sách bãi đỗ đã được cập nhật")
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Lỗi khi làm mới bãi đỗ: ${e.message}")
                _parkingLots.value = emptyList()
            } finally {
                isRefreshing.value = false
            }
        }
    }

    private suspend fun getDistanceToParkingLot(currentLocation: Location, baiDo: BaiDo): Double {
        return try {
            withContext(Dispatchers.IO) {
                val result = CompletableDeferred<Double>()
                val context = getApplication<Application>().applicationContext

                OpenRouteServiceHelper.getDistance(
                    context = context,
                    startLat = currentLocation.latitude,
                    startLng = currentLocation.longitude,
                    endLat = baiDo.latitude,
                    endLng = baiDo.longitude,
                    onSuccess = { distance ->
                        result.complete(distance)
                    },
                    onFailure = { error ->
                        Log.e("HomeViewModel", "Lỗi khi lấy khoảng cách: $error")
                        result.complete(Double.MAX_VALUE)
                    }
                )
                result.await()
            }
        } catch (e: Exception) {
            Log.e("HomeViewModel", "Lỗi khi gọi hàm getDistanceToParkingLot: ${e.message}")
            Double.MAX_VALUE
        }
    }

    fun onRefresh() {
        viewModelScope.launch {
            isRefreshing.value = true
            try {
                getCurrentLocation()
            } finally {
                isRefreshing.value = false
            }
        }
    }

    fun calculateDistance(
        context: Context, lat1: Double, lng1: Double, lat2: Double, lng2: Double,
        onResult: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val distance = withContext(Dispatchers.IO) {
                    val result = CompletableDeferred<Double>()
                    OpenRouteServiceHelper.getDistance(
                        context, lat1, lng1, lat2, lng2,
                        onSuccess = { distance -> result.complete(distance) },
                        onFailure = { error ->
                            Log.e("HomeViewModel", error)
                            result.complete(Double.MAX_VALUE)
                        }
                    )
                    result.await()
                }
                val distanceText = if (distance != Double.MAX_VALUE) {
                    "${String.format("%.3f",(distance / 1000))}"
                } else {
                    "Không xác định"
                }
                onResult(distanceText)
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error: ${e.message}")
                onResult("Lỗi tính khoảng cách")
            }
        }
    }


}
