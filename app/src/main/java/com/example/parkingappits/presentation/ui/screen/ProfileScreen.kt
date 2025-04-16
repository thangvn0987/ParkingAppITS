package com.example.parkingappits.presentation.ui.screen

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.parkingappits.R
import com.example.parkingappits.data.model.User
import com.example.parkingappits.data.repository.UserRepository
import com.example.parkingappits.presentation.viewmodel.ProfileViewModel
import com.example.parkingappits.presentation.viewmodel.ProfileViewModelFactory
//import kotlin.coroutines.jvm.internal.CompletedContinuation.context

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val activity = context as Activity

    val userRepository = remember { UserRepository(context) }
    val viewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModelFactory(userRepository)
    )

    // Nhận người dùng từ StateFlow (luôn đảm bảo không bị null)
    val userState by viewModel.user.collectAsState()

    // ActivityResultLauncher cho Google Sign-In
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.handleLoginResult(
                data = result.data,
                onSuccess = {
                    Toast.makeText(context, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
                },
                onFailure = { error ->
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }
            )
        } else {
            Toast.makeText(context, "Đăng nhập bị hủy", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadUser()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Sử dụng biến tạm để kiểm tra null
                val user = userState
                if (user != null) {
                    ShowProfileScreen(
                        user = user,
                        onLogout = {
                            viewModel.logout()
                            Toast.makeText(context, "Đăng xuất thành công!", Toast.LENGTH_SHORT).show()

                        },
                        viewModel
                    )
                } else {
                    ShowLoginScreen(
                        onLoginClicked = {
                            viewModel.login(activity) { launcher.launch(it) }
                        }
                    )
                }
            }
        }
    )
}

@Composable
fun ShowLoginScreen(onLoginClicked: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = onLoginClicked,
            modifier = Modifier
                .padding(8.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.icgg),
                    contentDescription = "Google Logo",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Đăng nhập bằng Google", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ShowProfileScreen(user: User, onLogout: () -> Unit, viewModel: ProfileViewModel) {
//    var vehicleNumber by remember { mutableStateOf("") }
    val vehicleNumber by viewModel.vehicleNumber.collectAsState()

    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Tên: ${user.name}", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = "Email: ${user.email}", fontSize = 15.sp)
        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = rememberImagePainter(user.photoUrl),
            contentDescription = "User Photo",
            modifier = Modifier
                .size(100.dp)
                .padding(8.dp)
        )

// TextField để nhập biển số xe
        OutlinedTextField(
            value = vehicleNumber,
            onValueChange = {
//                vehicleNumber = it
               viewModel.saveVehicleNumber(context, it)
                            },
            label = { Text("Biển số xe") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

//        Button(
//            onClick = {
//                viewModel.saveVehicleNumber(context, vehicleNumber)
//                Toast.makeText(context, "Đã lưu biển số xe!", Toast.LENGTH_SHORT).show()
//            },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(8.dp)
//        ) {
//            Text("Lưu biển số xe")
//        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onLogout) {
            Text("Đăng xuất")
        }

    }
}
