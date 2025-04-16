package com.example.parkingappits.data.model

data class ParkingDetail(
    val id: String = "",               // Mã định danh duy nhất của bãi đỗ
    val name: String = "",             // Tên bãi đỗ
    val location: String = "",         // Địa chỉ bãi đỗ
    val latitude: Double = 0.0,        // Vĩ độ
    val longitude: Double = 0.0,       // Kinh độ
    val totalSlots: Int = 0,           // Tổng số chỗ
    val availableSlots: Int = 0,       // Số chỗ còn trống
    val pricePerHour: Double = 0.0,    // Giá theo giờ
    val isOpen: Boolean = false,       // Trạng thái mở cửa
    val openingHours: String = "",     // Giờ mở cửa
    val contactNumber: String = "",    // Số điện thoại liên hệ
    val rating: Float = 0.0f,          // Đánh giá trung bình
    val amenities: List<String> = emptyList(), // Tiện ích
    val images: List<String> = emptyList(),    // URL các ảnh của bãi đỗ
    val description: String = ""       // Mô tả chi tiết
) {
    // Constructor không tham số cần thiết cho Firebase
    constructor() : this(
        id = "",
        name = "",
        location = "",
        latitude = 0.0,
        longitude = 0.0,
        totalSlots = 0,
        availableSlots = 0,
        pricePerHour = 0.0,
        isOpen = false,
        openingHours = "",
        contactNumber = "",
        rating = 0.0f,
        amenities = emptyList(),
        images = emptyList(),
        description = ""
    )
}
