package com.example.parkingappits.presentation.ui.screen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.parkingappits.data.repository.UserRepository
import com.example.parkingappits.presentation.viewmodel.HomeViewModel
import com.example.parkingappits.presentation.viewmodel.ParkingDetailViewModel
import com.example.parkingappits.presentation.viewmodel.ProfileViewModel
import com.example.parkingappits.presentation.viewmodel.ProfileViewModelFactory
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ParkingDetailScreen(
    navController: NavController,
    parkingId: String
) {
//    val context = LocalContext.current
    val viewModelhome: HomeViewModel = viewModel()
    val viewModel: ParkingDetailViewModel = viewModel()
    val context = LocalContext.current
    val repository = remember { UserRepository(context) }
    val factory = remember { ProfileViewModelFactory(repository) }
    val viewModelprofile: ProfileViewModel = viewModel(factory = factory)
    val parkingDetail by viewModel.parkingDetail.collectAsState()
    val imageUrls by viewModel.imageUrls.collectAsState()


    //nguoidung
    //bienso
    val user by viewModelprofile.user.collectAsState()

    val vehicleNumber by viewModelprofile.vehicleNumber.collectAsState()

    LaunchedEffect(parkingId) {
        viewModel.loadParkingDetail(parkingId)
        viewModelprofile.loadUser()
        viewModel.fetchParkingImages(parkingId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chi tiết bãi đỗ") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                if (user != null && vehicleNumber.isNotEmpty()) {
                    Button(

                        onClick = { /*   - Chưa triển khai */
                            viewModel.checkAndConfirmReservationWithSlotUpdate(
                                userId = user!!.id,
                                parkingId = parkingId,
                                slotNumber = 1,
                                vehicleNumber = vehicleNumber,
                                onAlreadyReserved = {
                                    viewModelhome.loadUserReservation()
                                    Toast.makeText(
                                        context,
                                        "Bạn đã có chỗ rồi.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                onReserved = {

                                    Toast.makeText(
                                        context,
                                        "Đặt chỗ thành công",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                },
                                onError = { message ->
                                    Toast.makeText(context, "Lỗi: $message", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text("XÁC NHẬN ĐẶT CHỖ")
                    }
                }
            }
        }
    ) { paddingValues ->
        parkingDetail?.let { detail ->
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                    ) {
                        if (viewModel.imageUrls.value.isNotEmpty()) {
                            LazyRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {

                                items(imageUrls) { imageUrl ->
                                    Image(
                                        painter = rememberAsyncImagePainter(model = imageUrl),
                                        contentDescription = "Parking Image",
                                        modifier = Modifier
                                            .width(300.dp)
                                            .height(200.dp)
                                    )
                                }
                            }
                        }

                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = "🅿️ ${detail.name}", style = MaterialTheme.typography.h5)
                    Text(text = "📍 Địa chỉ: ${detail.location}")
                    Text(text = "💰 Giá theo giờ: ${detail.pricePerHour.toInt()} K VND")
                    Text(text = "⏰ Giờ mở cửa: ${detail.openingHours}")
                    Text(text = "📞 Liên hệ: ${detail.contactNumber}")
                    Text(text = "🚗 Tổng chỗ: ${detail.totalSlots}")
                    Text(text = "🚩 Chỗ trống: ${detail.availableSlots}")

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "🌟 Đánh giá:")
                    RatingBar(rating = detail.rating)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "✔️ Tiện ích:")
                    detail.amenities.forEach { amenity ->
                        Text(text = "- $amenity")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "📄 Mô tả:")
                    Text(text = detail.description)
                }
            }
        } ?: run {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun RatingBar(rating: Float) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        repeat(5) { index ->
            val starColor = if (index < rating.toInt()) Color.Yellow else Color.Gray
            Icon(
                painter = painterResource(id = android.R.drawable.star_big_on),
                contentDescription = "Star",
                tint = starColor,
                modifier = Modifier.size(24.dp)
            )
        }
        Text(text = " (${String.format("%.1f", rating)})", style = MaterialTheme.typography.body1)
    }
}
