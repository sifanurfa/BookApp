package com.example.bookapp.admin

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.DialogFragment
import com.example.bookapp.R
import com.example.bookapp.databinding.ActivityAdminEditDataBinding
import com.example.bookapp.model.Books
import com.example.bookapp.network.ApiClient
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class AdminEditData : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    private lateinit var binding:ActivityAdminEditDataBinding
    private var id: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminEditDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getStringExtra("id")
        val judul = intent.getStringExtra("judul")
        val penulis = intent.getStringExtra("penulis")
        val tanggal_terbit = intent.getStringExtra("tanggal_terbit")
        val penerbit = intent.getStringExtra("penerbit")
        val jumlah_halaman = intent.getIntExtra("jumlah_halaman", 0)
        val harga = intent.getIntExtra("harga", 0)
        val sinopsis = intent.getStringExtra("sinopsis")
        val gambar = intent.getStringExtra("gambar")

        with(binding) {
            edtJudul.setText(judul.toString())
            edtPenulis.setText(penulis.toString())
            edtTglTerbit.setText(tanggal_terbit.toString())
            edtPenerbit.setText(penerbit.toString())
            edtJumlahHalaman.setText(jumlah_halaman.toString())
            edtHarga.setText(harga.toString())
            edtSinopsis.setText(sinopsis.toString())
            edtGambar.setText(gambar.toString())

            edtTglTerbit.setOnClickListener {
                val datePicker = DatePicker()
                datePicker.show(supportFragmentManager, "datePicker")
            }

            btnSimpanData.setOnClickListener {
                val judul = edtJudul.text.toString()
                val penulis = edtPenulis.text.toString()
                val tanggal_terbit = edtTglTerbit.text.toString()
                val penerbit = edtPenerbit.text.toString()
                val jumlah_halaman = edtJumlahHalaman.text.toString()
                val harga = edtHarga.text.toString()
                val sinopsis = edtSinopsis.text.toString()
                val gambar = edtGambar.text.toString()

                editDataBooks(judul, penulis, tanggal_terbit, penerbit, jumlah_halaman, harga, sinopsis, gambar)
            }

            icBackToHome.setOnClickListener {
                finish()
            }
        }
    }
    override fun onDateSet(view: android.widget.DatePicker?,
                           year: Int,
                           month:Int,
                           day: Int) {
        val tanggal = "$day/${month + 1}/$year"
        binding.edtTglTerbit.setText(tanggal)
    }

    private fun editDataBooks(
        judul: String,
        penulis: String,
        tanggal: String,
        penerbit: String,
        halaman: String,
        harga: String,
        sinopsis: String,
        gambar: String
    ) {
        //        CREATE REQUEST BODY
        val jsonData = Gson().toJson(
            mapOf(
                "judul" to judul,
                "penulis" to penulis,
                "tanggal_terbit" to tanggal,
                "penerbit" to penerbit,
                "jumlah_halaman" to halaman,
                "harga" to harga,
                "sinopsis" to sinopsis,
                "gambar" to gambar
            )
        )
        val requestBody = jsonData.toRequestBody("application/json".toMediaType())

//        RUN EDIT DATA
        val client = ApiClient.getInstance()
        val response = client.updateBooks(id.toString(), requestBody)
        response.enqueue(object: Callback<Books> {
            override fun onResponse(call: Call<Books>, response: Response<Books>) {
                if (response.isSuccessful) {
                    setResult(Activity.RESULT_OK)
                    finish()
                    Toast.makeText(this@AdminEditData, "Berhasil mengubah data!", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("API Error", "Response not successful or body is null")
                }
            }

            override fun onFailure(call: Call<Books>, t: Throwable) {
                Toast.makeText(this@AdminEditData, "Koneksi error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

