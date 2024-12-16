package com.example.bookapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bookapp.admin.AdminHomeActivity
import com.example.bookapp.databinding.ActivityRegisterBinding
import com.example.bookapp.user.UserHomeActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager.getInstance(this)
        with(binding) {
            btnRegister.setOnClickListener {
                val username = edtUsername.text.toString()
                val password = edtPassword.text.toString()
                val confirmPassword = edtConfirmPassword.text.toString()
                val role = edtRole.text.toString().lowercase()
                if (role != "user" && role != "admin") {
                    Toast.makeText(this@RegisterActivity, "Role harus 'user' atau 'admin'", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (username.isEmpty() || password.isEmpty() ||
                    confirmPassword.isEmpty()) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Mohon isi semua data",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (password != confirmPassword) {
                    Toast.makeText(this@RegisterActivity, "Password tidak sama",
                        Toast.LENGTH_SHORT)
                        .show()
                } else {
                    prefManager.saveUsername(username)
                    prefManager.savePassword(password)
                    prefManager.saveRole(role)
                    prefManager.setLoggedIn(true)
                    checkLoginStatus()
                }
            }
            txtRegisterToLogin.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            }
        }
    }
    private fun checkLoginStatus() {
        val isLoggedIn = prefManager.isLoggedIn()
        if (isLoggedIn) {
            val role = prefManager.getRole()
            when (role) {
                "user" -> {
                    Toast.makeText(this@RegisterActivity, "Login sebagai User berhasil", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@RegisterActivity, UserHomeActivity::class.java))
                }
                "admin" -> {
                    Toast.makeText(this@RegisterActivity, "Login sebagai Admin berhasil", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@RegisterActivity, AdminHomeActivity::class.java))
                }
                else -> {
                    Toast.makeText(this@RegisterActivity, "Role tidak dikenali", Toast.LENGTH_SHORT).show()
                }
            }
            finish()
        } else {
            Toast.makeText(this@RegisterActivity, "Login gagal", Toast.LENGTH_SHORT).show()
        }
    }
}