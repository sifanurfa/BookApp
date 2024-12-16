package com.example.bookapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bookapp.model.Books

@Database(entities = [Books::class], version = 1, exportSchema = false)
abstract class DatabaseInstance : RoomDatabase() {
    abstract fun booksDao(): BookDao?
    companion object {
        @Volatile
        private var INSTANCE: DatabaseInstance? = null
        fun getDatabase(context: Context): DatabaseInstance? {
            if (INSTANCE == null) {
                synchronized(DatabaseInstance::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        DatabaseInstance::class.java, "Book_Database"
                    )
                        .build()
                }
            }
            return INSTANCE
        }
    }
}