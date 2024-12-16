package com.example.bookapp.admin

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.DialogFragment
import com.example.bookapp.R
import com.example.bookapp.databinding.ActivityAdminTambahDataBinding
import com.example.bookapp.model.Books
import com.example.bookapp.network.ApiClient
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class AdminTambahData : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    private lateinit var binding:ActivityAdminTambahDataBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminTambahDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            edtTglTerbit.setOnClickListener {
                val datePicker = DatePicker()
                datePicker.show(supportFragmentManager, "datePicker")
            }

            icBackToHome.setOnClickListener {
                finish()
            }

            btnTambahData.setOnClickListener {
                val judul = edtJudul.text.toString()
                val penulis = edtPenulis.text.toString()
                val tanggal_terbit = edtTglTerbit.text.toString()
                val penerbit = edtPenerbit.text.toString()
                val jumlah_halaman = edtJumlahHalaman.text.toString()
                val harga = edtHarga.text.toString()
                val sinopsis = edtSinopsis.text.toString()
                val gambar = edtGambar.text.toString()
                addDataBooks(judul, penulis, tanggal_terbit, penerbit, jumlah_halaman, harga, sinopsis, gambar)
            }
        }
    }

    private fun addDataBooks(
        judul: String,
        penulis: String,
        tanggal: String,
        penerbit: String,
        halaman: String,
        harga: String,
        sinopsis: String,
        gambar: String,
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

//        RUN POST DATA
        val client = ApiClient.getInstance()
        val response = client.postNewBooks(requestBody)
        response.enqueue(object: Callback<Books> {
            override fun onResponse(call: Call<Books>, response: Response<Books>) {
                if (response.isSuccessful) {
                    setResult(Activity.RESULT_OK)
                    finish()
                    Toast.makeText(this@AdminTambahData, "Berhasil menambah data!", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("API Error", "Response not successful or body is null")
                }
            }

            override fun onFailure(call: Call<Books>, t: Throwable) {
                Toast.makeText(this@AdminTambahData, "Koneksi error", Toast.LENGTH_SHORT).show()
            }
        })
    }
    override fun onDateSet(view: android.widget.DatePicker?,
                           year: Int,
                           month:Int,
                           day: Int) {
        val tanggal = "$day/${month + 1}/$year"
        binding.edtTglTerbit.setText(tanggal)
    }
}

class DatePicker: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val monthOfYear = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(
            requireActivity(),
            activity as DatePickerDialog.OnDateSetListener,
            year,
            monthOfYear,
            dayOfMonth
        )
    }
}