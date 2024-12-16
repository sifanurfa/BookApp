package com.example.bookapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bookapp.admin.AdminHomeActivity
import com.example.bookapp.user.UserHomeActivity
import com.example.bookapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var prefManager: PrefManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager.getInstance(this)
        checkLoginStatus()
        with(binding) {
            btnAdmin.setOnClickListener {
                val intent = Intent(this@MainActivity, AdminHomeActivity::class.java)
                startActivity(intent)
            }
            btnUser.setOnClickListener {
                val intent = Intent(this@MainActivity, UserHomeActivity::class.java)
                startActivity(intent)
            }
        }
    }
    fun checkLoginStatus() {
        val isLoggedIn = prefManager.isLoggedIn()
        if (!isLoggedIn) {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }
    }
}