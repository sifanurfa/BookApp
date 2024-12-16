package com.example.bookapp.admin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookapp.LoginActivity
import com.example.bookapp.PrefManager
import com.example.bookapp.databinding.ActivityAdminHomeBinding
import com.example.bookapp.model.Books
import com.example.bookapp.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminHomeActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAdminHomeBinding
    private lateinit var prefManager: PrefManager
    private lateinit var adapterBooks: AdminBookAdapter
    private val bookList = ArrayList<Books>()
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefManager = PrefManager.getInstance(this)
        binding = ActivityAdminHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // Memuat ulang data buku dari API
                fetchBooksFromApi()
                Toast.makeText(this, "Data berhasil diperbarui!", Toast.LENGTH_SHORT).show()
            }
        }

        adapterBooks = AdminBookAdapter(bookList, ApiClient.getInstance(),
            onEditBooks = { intent ->
                activityResultLauncher.launch(intent)
            })

        fetchBooksFromApi()
        with(binding) {
            btnTambahData.setOnClickListener {
                val intent = Intent(this@AdminHomeActivity, AdminTambahData::class.java)
                activityResultLauncher.launch(intent)
            }
            rvBooks.apply {
                adapter = adapterBooks
                layoutManager = LinearLayoutManager(this@AdminHomeActivity)
            }
            btnLogout.setOnClickListener {
                prefManager.setLoggedIn(false)
                startActivity(Intent(this@AdminHomeActivity, LoginActivity::class.java))
                finish()
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
                Toast.makeText(this@AdminHomeActivity, "Koneksi error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}