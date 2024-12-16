package com.example.bookapp.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookapp.LoginActivity
import com.example.bookapp.PrefManager
import com.example.bookapp.databinding.ActivityUserHomeBinding
import com.example.bookapp.model.Books
import com.example.bookapp.model.User
import com.example.bookapp.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserHomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserHomeBinding
    private lateinit var prefManager: PrefManager
    private lateinit var adapterBooks: UserBookAdapter
    private val bookList = ArrayList<Books>()
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefManager = PrefManager.getInstance(this)
        binding = ActivityUserHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activityResultLauncher = registerForActivityResult(
            androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                // Tangani hasil dari aktivitas jika diperlukan
                val data = result.data
                Toast.makeText(this, "Hasil diterima!", Toast.LENGTH_SHORT).show()
            }
        }

        adapterBooks = UserBookAdapter(bookList, ApiClient.getInstance(),
            onEditBooks = { intent ->
                activityResultLauncher.launch(intent)
            })

        fetchBooksFromApi()
        with(binding) {
            rvBooks.apply {
                adapter = adapterBooks
                layoutManager = LinearLayoutManager(this@UserHomeActivity)
            }
            btnLogout.setOnClickListener {
                prefManager.setLoggedIn(false)
                startActivity(Intent(this@UserHomeActivity, LoginActivity::class.java))
                finish()
            }
            btnKeranjang.setOnClickListener {
                val intent = Intent(this@UserHomeActivity, UserKeranjang::class.java)
                startActivity(intent)
            }
        }
    }
    private fun fetchBooksFromApi() {
        binding.progressBar.visibility = View.VISIBLE
        val client = ApiClient.getInstance()
        val response = client.getAllBooks()

        response.enqueue(object : Callback<List<Books>> {
            override fun onResponse(call: Call<List<Books>>, response: Response<List<Books>>) {
                binding.progressBar.visibility = View.GONE
                if (response.isSuccessful && response.body() != null) {
                    val updatedBookList = response.body() ?: emptyList()
                    adapterBooks.updateData(updatedBookList)
                } else {
                    Log.e("API Error", "Response not successful or body is null")
                }
            }

            override fun onFailure(call: Call<List<Books>>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Log.e("API Error", "Koneksi error: ${t.message}")
                Toast.makeText(this@UserHomeActivity, "Koneksi error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}