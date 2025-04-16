package com.example.parkingappits.presentation.viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.parkingappits.data.model.User
import com.example.parkingappits.data.repository.UserRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> get() = _user

    private val _vehicleNumber = MutableStateFlow("")
    val vehicleNumber: StateFlow<String> get() = _vehicleNumber

    class ProfileViewModelFactory(
        private val repository: UserRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ProfileViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }


    fun loadUser() {
        _user.value = userRepository.getLoggedInUser()
        _vehicleNumber.value = userRepository.getVehicleNumber()
    }

    fun saveVehicleNumber(context: Context, number: String) {
        viewModelScope.launch {
            userRepository.saveVehicleNumber(context, number)
            _vehicleNumber.value = number
        }
    }


    // Đăng nhập bằng Google
    fun login(activity: Activity, launcher: (Intent) -> Unit) {
        val googleSignInClient = GoogleSignIn.getClient(
            activity,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken("551250061512-3a4tgmb1iam330pbu8jhkk4t3dut3emg.apps.googleusercontent.com")
                .build()
        )
        launcher(googleSignInClient.signInIntent)
    }

    // Xử lý kết quả đăng nhập
    fun handleLoginResult(data: Intent?, onSuccess: (User) -> Unit, onFailure: (String) -> Unit) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(com.google.android.gms.common.api.ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)

            auth.signInWithCredential(credential)
                .addOnSuccessListener { result ->
                    val firebaseUser = result.user
                    val user = User(
                        id = firebaseUser?.uid ?: "",
                        name = firebaseUser?.displayName ?: "Unknown",
                        email = firebaseUser?.email ?: "Unknown",
                        photoUrl = firebaseUser?.photoUrl.toString(),
//                        biensoxe = ""
                    )
                    userRepository.saveUserToPrefs(user)
                    onSuccess(user)
                }
                .addOnFailureListener { exception ->
                    onFailure("Login failed: ${exception.message}")
                }
        } catch (e: Exception) {
            onFailure("Error: ${e.message}")
        }
    }

    // Đăng xuất
    fun logout() {
        auth.signOut()
        userRepository.clearUserPrefs()
        _user.value = null
    }

}

class ProfileViewModelFactory(private val userRepository: UserRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}