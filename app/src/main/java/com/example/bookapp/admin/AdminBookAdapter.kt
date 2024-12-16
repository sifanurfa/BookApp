package com.example.bookapp.admin

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.bookapp.databinding.AdminListItemBinding
import com.example.bookapp.model.Books
import com.example.bookapp.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminBookAdapter(
    private val listBooks: ArrayList<Books>,
    private val client: ApiService,
    private val onEditBooks: (Intent) -> Unit,
) :
    RecyclerView.Adapter<AdminBookAdapter.ItemBookViewHolder>() {
        inner class ItemBookViewHolder(private val binding: AdminListItemBinding) :
                RecyclerView.ViewHolder(binding.root) {
                    fun bind(data: Books) {
                        with(binding) {
                            txtJudul.text = data.judul
                            txtPenulis.text = data.penulis
                            root.setOnClickListener {
                                val intentToEdit = Intent(itemView.context, AdminDetailData::class.java)
                                intentToEdit.putExtra("id", data.id ?: -1) // Default value jika null
                                intentToEdit.putExtra("judul", data.judul ?: "Tidak ada judul")
                                intentToEdit.putExtra("penulis", data.penulis ?: "Tidak ada penulis")
                                intentToEdit.putExtra("penerbit", data.penerbit ?: "Tidak ada penerbit")
                                intentToEdit.putExtra("tanggal_terbit", data.tanggal_terbit ?: "Tidak ada tanggal")
                                intentToEdit.putExtra("jumlah_halaman", data.jumlah_halaman ?: 0)
                                intentToEdit.putExtra("harga", data.harga ?: 0.0)
                                intentToEdit.putExtra("sinopsis", data.sinopsis ?: "Tidak ada sinopsis")
                                intentToEdit.putExtra("gambar", data.gambar ?: "Tidak ada gambar")
                                onEditBooks(intentToEdit)
                            }

                            btnHapus.setOnClickListener {
                                deleteItem(data = data, itemView, adapterPosition)
                            }
                        }
                    }
                }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemBookViewHolder {
        val binding = AdminListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemBookViewHolder(binding)
    }

    override fun getItemCount(): Int = listBooks.size

    override fun onBindViewHolder(holder: ItemBookViewHolder, position: Int) {
        holder.bind(listBooks[position])
    }

    fun deleteItem(data: Books, itemView: View, adapterPosition: Int){
        val response = data.id?.let { it1 -> client.deleteBooks(it1) }

        if (response != null) {
            response.enqueue(object : Callback<Books> {
                override fun onResponse(call: Call<Books>, response: Response<Books>) {
                    if (response.isSuccessful) {
                        Toast.makeText(itemView.context, "data buku ${data.judul} berhasil dihapus", Toast.LENGTH_SHORT).show()
                        val position = adapterPosition
                        if (position != RecyclerView.NO_POSITION) {
                            removeItem(position)
                        }
                    } else {
                        Log.e("API Error", "Response not successful or body is null")
                    }
                }
                override fun onFailure(call: Call<Books>, t: Throwable) {
                    Toast.makeText(itemView.context, "Koneksi error",
                        Toast.LENGTH_LONG).show()
                }
            })

        }
    }

    fun updateData(newList: List<Books>) {
        listBooks.clear()
        listBooks.addAll(newList)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        listBooks.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, listBooks.size)
    }
}