package com.example.bookapp.user

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.bookapp.R
import com.example.bookapp.model.Books
import com.squareup.picasso.Picasso

class KeranjangAdapter(
    private val context: Context,
    private val books: List<Books>, // Hanya list untuk menampilkan data
    private val onItemClick: (Books) -> Unit // Callback untuk klik item
) : BaseAdapter() {

    override fun getCount(): Int = books.size

    override fun getItem(position: Int): Books = books[position]

    override fun getItemId(position: Int): Long = position.toLong() // Pakai posisi sebagai ID unik

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        // Gunakan ViewHolder untuk efisiensi
        val viewHolder: ViewHolder
        val view: View

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.user_list_item, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        // Dapatkan item buku berdasarkan posisi
        val book = getItem(position)

        // Tampilkan data di tampilan
        viewHolder.txtJudul.text = book.judul
        viewHolder.txtHarga.text = "Rp${book.harga}" // Format harga

//        Picasso.get()
//            .load(book.gambar)
//            .into(viewHolder.imgBook)

        view.setOnClickListener {
            onItemClick(book) // Panggil callback dengan data buku
        }

        return view
    }

    // ViewHolder untuk menyimpan referensi elemen tampilan
    private class ViewHolder(view: View) {
        val txtJudul: TextView = view.findViewById(R.id.txt_judul)
        val txtHarga: TextView = view.findViewById(R.id.txt_harga)
        val imgBook: ImageView = view.findViewById(R.id.img_buku)
    }
}