package com.example.parkingappits.data.model

/*import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider*/

data class User(
//    var biensoxe: String,
    val id: String,
    val name: String,
    val email: String,
    val photoUrl: String
)
//nhap
//private fun signInWithGoogle(context: Context, onLoginSuccess: (User) -> Unit) {
//    val auth = FirebaseAuth.getInstance()
//
//    val googleSignInClient = com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(
//        context,
//        com.google.android.gms.auth.api.signin.GoogleSignInOptions.Builder(com.google.android.gms.auth.api.signin.GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestEmail()
//            .requestIdToken("551250061512-kaimsf8i5kmvmvjk9vegaec74hs5kenl.apps.googleusercontent.com") // Thay bằng ID từ Firebase Console
//            .build()
//    )
//
//    // Đăng ký ActivityResultLauncher
//    val activity = context as androidx.activity.ComponentActivity
//    val launcher = activity.registerForActivityResult(
//        androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
//    ) { result ->
//        // Sử dụng Activity.RESULT_OK thay vì RESULT_OK trực tiếp
//        if (result.resultCode == android.app.Activity.RESULT_OK) {
//            val task = com.google.android.gms.auth.api.signin.GoogleSignIn.getSignedInAccountFromIntent(result.data)
//            try {
//                val account = task.getResult(com.google.android.gms.common.api.ApiException::class.java)
//                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
//
//                auth.signInWithCredential(credential)
//                    .addOnSuccessListener { authResult ->
//                        val firebaseUser = authResult.user
//                        val user = User(
//                            id = firebaseUser?.uid ?: "",
//                            name = firebaseUser?.displayName ?: "Không rõ",
//                            email = firebaseUser?.email ?: "Không rõ",
//                            photoUrl = firebaseUser?.photoUrl.toString()
//                        )
//                        onLoginSuccess(user)
//                    }
//                    .addOnFailureListener { exception ->
//                        Toast.makeText(context, "Đăng nhập thất bại: ${exception.message}", Toast.LENGTH_SHORT).show()
//                    }
//            } catch (e: Exception) {
//                Toast.makeText(context, "Lỗi đăng nhập: ${e.message}", Toast.LENGTH_SHORT).show()
//            }
//        } else {
//            Toast.makeText(context, "Hủy đăng nhập", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    // Khởi chạy đăng nhập
//    val signInIntent = googleSignInClient.signInIntent
//    launcher.launch(signInIntent)
//}
//
//
//
//
//
//private fun saveUserToPrefs(sharedPrefs: SharedPreferences, user: User) {
//    with(sharedPrefs.edit()) {
//        putString("id", user.id)
//        putString("name", user.name)
//        putString("email", user.email)
//        putString("photoUrl", user.photoUrl)
//        apply()
//    }
//}
//
//private fun getLoggedInUser(sharedPrefs: SharedPreferences): User? {
//    val id = sharedPrefs.getString("id", null) ?: return null
//    val name = sharedPrefs.getString("name", "Không rõ") ?: "Không rõ"
//    val email = sharedPrefs.getString("email", "Không rõ") ?: "Không rõ"
//    val photoUrl = sharedPrefs.getString("photoUrl", "") ?: ""
//    return User(id, name, email, photoUrl)
//}
//
//private fun clearUserPrefs(sharedPrefs: SharedPreferences) {
//    with(sharedPrefs.edit()) {
//        clear()
//        apply()
//    }
//}