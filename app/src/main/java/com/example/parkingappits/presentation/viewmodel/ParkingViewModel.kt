import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

// ParkingState - Trạng thái bãi đỗ
data class ParkingState(
    val parkingSlots: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

// ParkingViewModel - Quản lý trạng thái bãi đỗ
class ParkingViewModel : ViewModel() {
    var state by mutableStateOf(ParkingState())
        private set

    fun loadParkingSlots() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            try {
                // Giả lập tải dữ liệu bãi đỗ
                val slots = listOf("A1", "B2", "C3", "D4")
                state = state.copy(parkingSlots = slots, isLoading = false)
            } catch (e: Exception) {
                state = state.copy(errorMessage = "Lỗi tải dữ liệu", isLoading = false)
            }
        }
    }
}

@Composable
fun ParkingScreen(viewModel: ParkingViewModel = ParkingViewModel()) {
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
