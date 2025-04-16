package com.example.parkingappits.utils

import android.content.Context
import android.content.SharedPreferences

fun getApiKey(context: Context): String {
    val applicationInfo = context.packageManager
        .getApplicationInfo(context.packageName, android.content.pm.PackageManager.GET_META_DATA)
    return applicationInfo.metaData.getString("com.google.android.geo.API_KEY") ?: "API_KEY_NOT_FOUND"
}

object SharedPrefsHelper {

    private const val USER_PREFS = "user_prefs"
    private const val HISTORY_PREFS = "history_prefs"

    // Hàm lấy SharedPreferences theo tên
    private fun getPreferences(context: Context, prefsName: String): SharedPreferences {
        return context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
    }

    // Lấy SharedPreferences cho người dùng
    fun getUserPrefs(context: Context): SharedPreferences {
        return getPreferences(context, USER_PREFS)
    }

    // Lấy SharedPreferences cho lịch sử
    fun getHistoryPrefs(context: Context): SharedPreferences {
        return getPreferences(context, HISTORY_PREFS)
    }

    // Hàm lưu dữ liệu vào SharedPreferences bất kỳ
    fun saveData(prefs: SharedPreferences, key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    // Hàm lấy dữ liệu từ SharedPreferences bất kỳ
    fun getData(prefs: SharedPreferences, key: String, defaultValue: String = ""): String {
        return prefs.getString(key, defaultValue) ?: defaultValue
    }

    // Hàm xóa dữ liệu từ SharedPreferences bất kỳ
    fun clearData(prefs: SharedPreferences) {
        prefs.edit().clear().apply()
    }
}
