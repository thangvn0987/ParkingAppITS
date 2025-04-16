package com.example.parkingappits.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.parkingappits.data.model.User

class UserRepository(context: Context) {
    private val sharedPrefs: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    // Lưu thông tin người dùng
    fun saveUserToPrefs(user: User) {
        with(sharedPrefs.edit()) {
            putString("id", user.id)
            putString("name", user.name)
            putString("email", user.email)
            putString("photoUrl", user.photoUrl)
            apply()
        }
    }
    fun saveVehicleNumber(context: Context, number: String) {
        sharedPrefs.edit().putString("biensoxe", number).apply()
    }
    fun getVehicleNumber(): String {
        return sharedPrefs.getString("biensoxe", "") ?: ""
    }
    // Lấy thông tin người dùng
    fun getLoggedInUser(): User? {
        val id = sharedPrefs.getString("id", null) ?: return null
        val name = sharedPrefs.getString("name", "Unknown") ?: "Unknown"
        val email = sharedPrefs.getString("email", "Unknown") ?: "Unknown"
        val photoUrl = sharedPrefs.getString("photoUrl", "") ?: ""
//        var biensoxe = sharedPrefs.getString("biensoxe","Chưa biển số") ?: "Chưa biển số"
        return User(id, name, email, photoUrl)
    }

    // Xóa thông tin người dùng
    fun clearUserPrefs() {
        with(sharedPrefs.edit()) {
            clear()
            apply()
        }
    }
}
