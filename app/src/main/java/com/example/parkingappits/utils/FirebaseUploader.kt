package com.example.parkingappits.utils


import com.example.parkingappits.data.model.ParkingDetail
import com.google.firebase.firestore.FirebaseFirestore

//data class ParkingDetail(
//    val id: String,
//    val name: String,
//    val location: String,
//    val latitude: Double,
//    val longitude: Double,
//    val totalSlots: Int,
//    val availableSlots: Int,
//    val pricePerHour: Double,
//    val isOpen: Boolean,
//    val openingHours: String,
//    val contactNumber: String,
//    val rating: Float,
//    val amenities: List<String>,
//    val images: List<String>,
//    val description: String
//)

fun uploadParkingData(parking: ParkingDetail) {
    val db = FirebaseFirestore.getInstance()
    db.collection("parkings").document(parking.id)
        .set(parking)
        .addOnSuccessListener { println("Successfully added: ${parking.name}") }
        .addOnFailureListener { e -> println("Error adding parking: ${e.message}") }
}

fun uploadAllParkings() {
    val parkings = listOf(
        ParkingDetail(
            id = "1",
            name = "NguyenMinhThang",
            location = "123 Đường Minh Thắng, Quận 9, TP.HCM",
            latitude = 10.851617,
            longitude = 106.771004,
            totalSlots = 10,
            availableSlots = 3,
            pricePerHour = 20.0,
            isOpen = true,
            openingHours = "8:00 - 22:00",
            contactNumber = "0123456789",
            rating = 4.5f,
            amenities = listOf("Wi-Fi", "Camera an ninh", "Nhân viên bảo vệ"),
            images = listOf(),
            description = "Bãi đỗ xe rộng rãi, có bảo vệ 24/24. Vị trí gần trung tâm."
        ),
        ParkingDetail(
            id = "2",
            name = "HuynhTrungTruc",
            location = "45 Đường Trung Trực, Quận Bình Thạnh",
            latitude = 10.850519,
            longitude = 106.663972,
            totalSlots = 8,
            availableSlots = 2,
            pricePerHour = 15.0,
            isOpen = true,
            openingHours = "7:00 - 21:00",
            contactNumber = "0987654321",
            rating = 4.0f,
            amenities = listOf("Sạc xe điện", "Nhà vệ sinh", "Hệ thống giám sát"),
            images = listOf(),
            description = "Bãi đỗ tiện nghi, có camera giám sát 24/24. Gần khu dân cư."
        ),
        ParkingDetail(
            id = "3",
            name = "NguyenThong",
            location = "78 Đường Nguyễn Thông, Quận 3, TP.HCM",
            latitude = 10.871453,
            longitude = 106.660625,
            totalSlots = 5,
            availableSlots = 2,
            pricePerHour = 25.0,
            isOpen = true,
            openingHours = "6:00 - 20:00",
            contactNumber = "0345678901",
            rating = 4.7f,
            amenities = listOf("Bãi xe có mái che", "Nhân viên bảo vệ", "Wi-Fi miễn phí"),
            images = listOf(),
            description = "Vị trí thuận tiện, gần trung tâm và khu mua sắm. Bãi đỗ có mái che."
        ),
        ParkingDetail(
            id = "4",
            name = "NguyenChiCong",
            location = "56 Đường Chí Công, Quận Phú Nhuận",
            latitude = 10.870088,
            longitude = 106.658122,
            totalSlots = 12,
            availableSlots = 0,
            pricePerHour = 18.0,
            isOpen = false,
            openingHours = "9:00 - 23:00",
            contactNumber = "0934567890",
            rating = 3.9f,
            amenities = listOf("Có bảo vệ", "Hệ thống chiếu sáng", "Bãi rộng rãi"),
            images = listOf(),
            description = "Bãi đỗ an toàn, nhiều chỗ để. Hiện tại đang đóng cửa."
        )
    )

    parkings.forEach { uploadParkingData(it) }
}
