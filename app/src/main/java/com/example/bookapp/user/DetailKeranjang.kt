package com.example.bookapp.user

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bookapp.R
import com.example.bookapp.database.BookDao
import com.example.bookapp.database.DatabaseInstance
import com.example.bookapp.databinding.ActivityDetailKeranjangBinding
import com.example.bookapp.model.Books
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DetailKeranjang : AppCompatActivity() {
    private lateinit var binding: ActivityDetailKeranjangBinding
    private lateinit var bookDao: BookDao
    private lateinit var executorService: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailKeranjangBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi database dan executor
        val db = DatabaseInstance.getDatabase(this)
        bookDao = db!!.booksDao()!!
        executorService = Executors.newSingleThreadExecutor()

        val id = intent.getStringExtra("id")
        val judul = intent.getStringExtra("judul")
        val penulis = intent.getStringExtra("penulis")
        val tanggal_terbit = intent.getStringExtra("tanggal_terbit")
        val penerbit = intent.getStringExtra("penerbit")
        val jumlah_halaman = intent.getIntExtra("jumlah_halaman", 0)
        val harga = intent.getIntExtra("harga", 0)
        val gambar = intent.getStringExtra("gambar")

        with(binding) {
            txtJudul.text = judul
            txtPenulis.text = penulis
            txtTglTerbit.text = tanggal_terbit
            txtPenerbit.text = penerbit
            txtJmlHalaman.text = "$jumlah_halaman halaman"
            btnBeli.text = "Beli Sekarang Rp${harga}"

            icBackToHome.setOnClickListener {
                finish()
            }

            btnBeli.setOnClickListener {
                executorService.execute {
                    val bookToDelete = Books(
                        id = id ?: "",
                        judul = judul ?: "",
                        penulis = penulis ?: "",
                        tanggal_terbit = tanggal_terbit ?: "",
                        penerbit = penerbit ?: "",
                        jumlah_halaman = jumlah_halaman,
                        harga = harga,
                        sinopsis = "",
                        gambar = gambar
                    )
                    bookDao.delete(bookToDelete) // Hapus dari Room
                    runOnUiThread {
                        Toast.makeText(this@DetailKeranjang, "Buku berhasil dibeli!", Toast.LENGTH_SHORT).show()
                        finish() // Kembali ke aktivitas sebelumnya
                    }
                }
            }
        }
    }
}