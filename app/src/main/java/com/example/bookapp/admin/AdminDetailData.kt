package com.example.bookapp.admin

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bookapp.R
import com.example.bookapp.databinding.ActivityAdminDetailDataBinding
import com.squareup.picasso.Picasso

class AdminDetailData : AppCompatActivity() {
    private lateinit var binding:ActivityAdminDetailDataBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDetailDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra("id")
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

            btnEdit.setOnClickListener {
                val intent = Intent(this@AdminDetailData, AdminEditData::class.java).apply {
                    putExtra("id", id)
                    putExtra("judul", judul)
                    putExtra("penulis", penulis)
                    putExtra("tanggal_terbit", tanggal_terbit)
                    putExtra("penerbit", penerbit)
                    putExtra("jumlah_halaman", jumlah_halaman)
                    putExtra("harga", harga)
                    putExtra("sinopsis", sinopsis)
                    putExtra("gambar", gambar)
                }
                startActivity(intent)
            }
        }
    }
}