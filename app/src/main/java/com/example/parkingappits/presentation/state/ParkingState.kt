package com.example.parkingapp.presentation.state

/**
 * Trạng thái của màn hình quản lý bãi đỗ.
 *
 * @property parkingSlots Danh sách các vị trí bãi đỗ (ở đây sử dụng kiểu String, bạn có thể mở rộng thành lớp dữ liệu riêng).
 * @property isLoading Cờ báo cho biết dữ liệu đang được tải.
 * @property errorMessage Thông báo lỗi nếu có.
 */
data class ParkingState(
    val parkingSlots: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
