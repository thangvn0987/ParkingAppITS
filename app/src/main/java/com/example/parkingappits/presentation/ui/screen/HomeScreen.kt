package com.example.parkingappits.presentation.ui.screen

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.parkingappits.data.model.BaiDo
import com.example.parkingappits.presentation.viewmodel.HomeViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = viewModel()) {
    val parkingLots by viewModel.parkingLots.collectAsState()
    val currentLocation by viewModel.currentLocation.collectAsState()
    val isUserLoggedIn by remember { mutableStateOf(viewModel.isUserLoggedIn()) }
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val reservation by viewModel.currentReservation.collectAsState()
    val context = LocalContext.current
    var elapsedMinutes by remember { mutableStateOf(0L) }

    LaunchedEffect(reservation?.timestamp) {
        if (reservation != null) {
            while (true) {
                val now = System.currentTimeMillis()
                val diff = now - (reservation?.timestamp ?: now)
                elapsedMinutes = diff / 60000
                delay(60000)
            }
        }
    }
    val baidohientai = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        if (isUserLoggedIn) {
            viewModel.onRefresh()
            viewModel.loadUserReservation()
            reservation?.let {
                baidohientai.value = viewModel.getParkingNameById(it.parkingId)
            }

        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🚘 Bãi đỗ gần bạn") },
                actions = {
                    if (reservation != null) {
                        TextButton(onClick = {




                        }) {
                            Text("🗺️ Bản đồ", color = Color.White)
                        }
                        TextButton(onClick = { navController.navigate("profile") }) {
                            Text("📜 Tài khoản", color = Color.White)
                        }
                    }
                }
            )
        },
        bottomBar = {
            reservation?.let { res ->
                val minutesToPay = (elapsedMinutes - 15).coerceAtLeast(0)
                val amount = minutesToPay * 1000

                Surface(
                    elevation = 8.dp,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("🕒 Đặt lúc: ${viewModel.formatTime(res.timestamp)}", color = Color.White)

                        }
                        Text("💸 Tiền phải trả: ${amount}đ", color = Color.White)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("📍 Bãi đỗ: ${baidohientai.value ?: "Đang tải..."}", color = Color.White)
                        }

                        Button(
                            onClick = {
                                viewModel.cancelReservation(
                                    onSuccess = {
                                        Toast.makeText(context, "Đã hủy đặt chỗ", Toast.LENGTH_SHORT).show()
                                    },
                                    onFailure = {
                                        Toast.makeText(context, "Hủy thất bại", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("❌ Hủy đặt chỗ")
                        }
                    }
                }
            } ?: run {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    Button(onClick = {



                    }) {
                        Text("🗺️ Bản đồ")
                    }
                    Button(onClick = { navController.navigate("profile") }) {
                        Text("📜 Tài khoản")
                    }
                }
            }
        }

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            when {
                !isUserLoggedIn -> {
                    Text("BẠN CHƯA ĐĂNG NHẬP", color = Color.Red, fontWeight = FontWeight.Bold)
                }

                currentLocation == null -> {
                    Text("Đang xác định vị trí...")
                }

                parkingLots.isEmpty() -> {
                    Text("Không có bãi đỗ khả dụng", color = Color.Gray)
                }

                else -> {
                    SwipeRefresh(
                        state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
                        onRefresh = {
                            if (isUserLoggedIn) {

                                viewModel.onRefresh()
                                viewModel.loadUserReservation()
                                reservation?.let {
                                    baidohientai.value = viewModel.getParkingNameById(it.parkingId)
                                }
                                Log.d("DEBUG_LOG", " co chay onrefesh  baidohientai is NULL")

                                if (baidohientai != null) {
                                    Log.d("DEBUG_LOG", " bai do hien tai khac null")
                                }
                                if (baidohientai == null) {
                                    Log.d("DEBUG_LOG", " bai do hien tai la null")
                                }
                            }
                        }
                    ) {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(parkingLots.size) { index ->
                                val baiDo = parkingLots[index]
                                var distanceText by remember { mutableStateOf("Đang tính...") }

                                LaunchedEffect(currentLocation, baiDo) {
                                    currentLocation?.let { location ->
                                        viewModel.calculateDistance(
                                            context = context,
                                            lat1 = location.latitude,
                                            lng1 = location.longitude,
                                            lat2 = baiDo.latitude,
                                            lng2 = baiDo.longitude
                                        ) { result ->
                                            distanceText = result
                                        }
                                    }
                                }

                                ParkingItem(navController, baiDo, distanceText)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ParkingItem(navController: NavController, baiDo: BaiDo, distance: String) {
    Card(
        modifier = Modifier
            .clickable { navController.navigate("parking_detail/${baiDo.id}") }
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "🅿️ ${baiDo.name} - " +
                            if (baiDo.available_Slots > 0) "Còn ${baiDo.available_Slots} chỗ" else "HẾT CHỖ",
                    color = if (baiDo.available_Slots > 0) MaterialTheme.colors.primary else Color.Red,
                    fontWeight = if (baiDo.available_Slots > 0) FontWeight.Normal else FontWeight.Bold
                )
                Text(text = "📍 Khoảng cách: ${distance} km")
            }
        }
    }
}