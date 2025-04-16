//package com.example.parkingappits.utils
//
//import com.example.parkingappits.data.model.User
//import com.example.parkingappits.domain.usecase.AuthUseCase
//
//class MockAuthUseCase(private val mockUser: User?) : AuthUseCase(null) {
//
//    override fun getLoggedInUser(): User? {
//        return mockUser
//    }
//
//    override fun signInWithGoogle(onSuccess: (User) -> Unit, onFailure: (String) -> Unit) {
//        mockUser?.let {
//            onSuccess(it)
//        } ?: onFailure("Mock user not found")
//    }
//
//    override fun logout() {
//        // Giả lập đăng xuất
//    }
//}
