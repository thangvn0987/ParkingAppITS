package com.example.parkingapp.presentation.ui.screen.parking

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parkingapp.presentation.viewmodel.ParkingViewModel

/**
 * Màn hình quản lý bãi đỗ.
 * Hiển thị danh sách các vị trí, trạng thái tải và xử lý lỗi nếu có.
 */
@Composable
fun ParkingScreen(viewModel: ParkingViewModel) {
    // Tự động tải dữ liệu khi màn hình được khởi tạo
    LaunchedEffect(Unit) {
        viewModel.loadParkingSlots()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quản lý bãi đỗ") },
                backgroundColor = Color(0xFF6200EE),
                contentColor = Color.White
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Danh sách các vị trí đỗ",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            if (viewModel.state.isLoading) {
                CircularProgressIndicator()
            } else if (viewModel.state.errorMessage != null) {
                Text(text = viewModel.state.errorMessage!!, color = Color.Red)
            } else {
                viewModel.state.parkingSlots.forEach { slot ->
                    Text(text = "Vị trí: $slot", modifier = Modifier.padding(4.dp))
                }
            }
            Button(
                onClick = { viewModel.loadParkingSlots() },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Tải danh sách bãi đỗ")
            }
        }
    }
}
