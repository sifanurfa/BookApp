package com.example.bookapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bookapp.admin.AdminHomeActivity
import com.example.bookapp.databinding.ActivityLoginBinding
import com.example.bookapp.user.UserHomeActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var prefManager: PrefManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager.getInstance(this)
        with(binding){
            btnLogin.setOnClickListener {
                val username = edtUsername.text.toString()
                val password = edtPassword.text.toString()
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Mohon isi semua data",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    if (isValidUsernamePassword()) {
                        prefManager.setLoggedIn(true)
                        checkLoginStatus()
                    } else {
                        Toast.makeText(this@LoginActivity, "Username atau password salah", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            txtLoginToRegister.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }
    }
    private fun isValidUsernamePassword(): Boolean{
        val username = prefManager.getUsername()
        val password = prefManager.getPassword()
        val inputUsername = binding.edtUsername.text.toString()
        val inputPassword = binding.edtPassword.text.toString()
        return username == inputUsername && password == inputPassword
    }
    private fun checkLoginStatus() {
        val isLoggedIn = prefManager.isLoggedIn()
        if (isLoggedIn) {
            val role = prefManager.getRole()
            when (role) {
                "user" -> {
                    Toast.makeText(this@LoginActivity, "Login sebagai User berhasil", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, UserHomeActivity::class.java))
                }
                "admin" -> {
                    Toast.makeText(this@LoginActivity, "Login sebagai Admin berhasil", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, AdminHomeActivity::class.java))
                }
                else -> {
                    Toast.makeText(this@LoginActivity, "Role tidak dikenali", Toast.LENGTH_SHORT).show()
                }
            }
            finish()
        } else {
            Toast.makeText(this@LoginActivity, "Login gagal", Toast.LENGTH_SHORT).show()
        }
    }

//    private fun checkLoginStatus() {
//        val isLoggedIn = prefManager.isLoggedIn()
//        if (isLoggedIn) {
//            Toast.makeText(this@LoginActivity, "Login berhasil",
//                Toast.LENGTH_SHORT).show()
//            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
//            finish()
//        } else {
//            Toast.makeText(this@LoginActivity, "Login gagal",
//                Toast.LENGTH_SHORT).show()
//        }
//    }
}