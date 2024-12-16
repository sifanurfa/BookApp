package com.example.bookapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity("books_db")
data class Books(
    @PrimaryKey
    @SerializedName("_id")
    val id: String,
    @SerializedName("judul")
    val judul: String,
    @SerializedName("penulis")
    val penulis: String,
    @SerializedName("tanggal_terbit")
    val tanggal_terbit: String,
    @SerializedName("penerbit")
    val penerbit: String,
    @SerializedName("jumlah_halaman")
    val jumlah_halaman: Int,
    @SerializedName("harga")
    val harga: Int,
    @SerializedName("sinopsis")
    val sinopsis: String,
    @SerializedName("gambar")
    val gambar: String? = null
)
