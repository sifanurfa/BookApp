package com.example.bookapp.user

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bookapp.R
import com.example.bookapp.database.BookDao
import com.example.bookapp.database.DatabaseInstance
import com.example.bookapp.databinding.ActivityUserDetailDataBinding
import com.example.bookapp.model.Books
import com.squareup.picasso.Picasso
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserDetailData : AppCompatActivity() {
    private lateinit var binding:ActivityUserDetailDataBinding
    private lateinit var bookDao: BookDao
    private lateinit var executorService: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        executorService = Executors.newSingleThreadExecutor()
        val db = DatabaseInstance.getDatabase(this)
        bookDao = db!!.booksDao()!!

        val judul = intent.getStringExtra("judul")
        val penulis = intent.getStringExtra("penulis")
        val tanggal_terbit = intent.getStringExtra("tanggal_terbit")
        val penerbit = intent.getStringExtra("penerbit")
        val jumlah_halaman = intent.getIntExtra("jumlah_halaman", 0)
        val harga = intent.getIntExtra("harga", 0)
        val sinopsis = intent.getStringExtra("sinopsis")
        val gambar = intent.getStringExtra("gambar")

        with(binding) {
            txtJudul.text = judul
            txtPenulis.text = penulis
            txtTglTerbit.text = tanggal_terbit
            txtPenerbit.text = penerbit
            txtJmlHalaman.text = "$jumlah_halaman halaman"
            txtHarga.text = "Rp${harga}"
            txtSinopsis.text = sinopsis

            Picasso.get()
                .load(gambar)
                .into(imgBook)

            icBackToHome.setOnClickListener {
                finish()
            }

            btnKeranjang.setOnClickListener {
                val book = Books(
                    id = System.currentTimeMillis().toString(),
                    judul = judul ?: "",
                    penulis = penulis ?: "",
                    tanggal_terbit = tanggal_terbit ?: "",
                    penerbit = penerbit ?: "",
                    jumlah_halaman = jumlah_halaman,
                    harga = harga,
                    sinopsis = sinopsis ?: "",
                    gambar = gambar
                )

                executorService.execute {
                    bookDao.insert(book)
                    runOnUiThread {
                        Toast.makeText(this@UserDetailData, "Buku berhasil ditambahkan ke keranjang!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}