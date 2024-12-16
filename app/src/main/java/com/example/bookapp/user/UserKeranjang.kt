package com.example.bookapp.user

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bookapp.R
import com.example.bookapp.database.BookDao
import com.example.bookapp.database.DatabaseInstance
import com.example.bookapp.databinding.ActivityUserKeranjangBinding
import com.example.bookapp.model.Books
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserKeranjang : AppCompatActivity() {
    private lateinit var binding: ActivityUserKeranjangBinding
    private lateinit var bookDao: BookDao
    private lateinit var executorService: ExecutorService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserKeranjangBinding.inflate(layoutInflater)
        setContentView(binding.root)

        executorService = Executors.newSingleThreadExecutor()
        val db = DatabaseInstance.getDatabase(this)
        bookDao = db!!.booksDao()!!

        getAllBooks()

        with(binding) {
            icBackToHome.setOnClickListener {
                finish()
            }
        }
    }
    private fun getAllBooks() {
        bookDao.allBooks.observe(this) { listBooks ->
            val adapter = KeranjangAdapter(
                context = this,
                books = listBooks,
                onItemClick = { book ->
                    // Buat intent ke UserDetailData
                    val intent = Intent(this, DetailKeranjang::class.java).apply {
                        putExtra("id", book.id)
                        putExtra("judul", book.judul)
                        putExtra("penulis", book.penulis)
                        putExtra("tanggal_terbit", book.tanggal_terbit)
                        putExtra("penerbit", book.penerbit)
                        putExtra("jumlah_halaman", book.jumlah_halaman)
                        putExtra("harga", book.harga)
                        putExtra("sinopsis", book.sinopsis)
                        putExtra("gambar", book.gambar)
                    }
                    startActivity(intent) // Mulai aktivitas detail
                }
            )

            binding.lvKeranjang.adapter = adapter // Pasang adapter ke ListView
        }
    }
}
