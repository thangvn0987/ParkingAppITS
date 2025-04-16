//package com.example.parkingappits.domain.usecase
//
//import com.example.parkingappits.data.model.User
//import com.example.parkingappits.data.repository.UserRepository
//
//open class AuthUseCase(private val userRepository: UserRepository?) {
//
//    open fun signInWithGoogle(
//        onSuccess: (User) -> Unit,
//        onFailure: (String) -> Unit
//    ) {
//        userRepository?.signInWithGoogle(onSuccess, onFailure)
//    }
//
//    open fun getLoggedInUser(): User? {
//        return userRepository?.getLoggedInUser()
//    }
//
//    open fun logout() {
//        userRepository.clearUserPrefs()
//    }
//}
