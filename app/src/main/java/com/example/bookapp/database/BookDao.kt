package com.example.bookapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.bookapp.model.Books

@Dao
interface BookDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(books: Books)
    @Update
    fun update(books: Books)
    @Delete
    fun delete(books: Books)
    @get:Query("SELECT * from books_db ORDER BY id ASC")
    val allBooks: LiveData<List<Books>>
}