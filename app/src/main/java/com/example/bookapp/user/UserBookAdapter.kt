package com.example.bookapp.user

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookapp.R
import com.example.bookapp.databinding.UserListItemBinding
import com.example.bookapp.model.Books
import com.example.bookapp.network.ApiService
import com.squareup.picasso.Picasso

class UserBookAdapter(
    private val listBooks: ArrayList<Books>,
    private val client: ApiService,
    private val onEditBooks: (Intent) -> Unit,
) :
    RecyclerView.Adapter<UserBookAdapter.ItemBookViewHolder>() {
    inner class ItemBookViewHolder(private val binding: UserListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Books) {
            with(binding) {
                txtJudul.text = data.judul
                txtHarga.text = data.harga.toString()

                Picasso.get()
                    .load(data.gambar)
                    .into(imgBuku)

                root.setOnClickListener {
                    val context = root.context
                    val intent = Intent(context, UserDetailData::class.java).apply {
                        putExtra("judul", data.judul)
                        putExtra("penulis", data.penulis)
                        putExtra("tanggal_terbit", data.tanggal_terbit)
                        putExtra("penerbit", data.penerbit)
                        putExtra("jumlah_halaman", data.jumlah_halaman)
                        putExtra("harga", data.harga)
                        putExtra("sinopsis", data.sinopsis)
                        putExtra("gambar", data.gambar)
                    }
                    onEditBooks(intent) // Callback untuk menjalankan intent
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemBookViewHolder {
        val binding = UserListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemBookViewHolder(binding)
    }

    override fun getItemCount(): Int = listBooks.size

    override fun onBindViewHolder(holder: ItemBookViewHolder, position: Int) {
        holder.bind(listBooks[position])
    }
    fun updateData(newList: List<Books>) {
        listBooks.clear()
        listBooks.addAll(newList)
        notifyDataSetChanged()
    }
}