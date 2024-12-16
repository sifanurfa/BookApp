package com.example.bookapp.network

import com.example.bookapp.model.Books
import com.example.bookapp.model.User
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("user")
//    fun getAllUsers(): Call<List<User>>
    fun getAllUsers(): Call<User>

    @GET("user/{id}")
    fun getUserById(@Path("id") bookId: String): Call<List<User>>

    @GET("books")
    fun getAllBooks(): Call<List<Books>>

    @POST("books")
    fun postNewBooks(@Body rawJson: RequestBody): Call<Books>

    @POST("books/{id}")
    fun updateBooks(@Path("id") bookId: String, @Body rawJson: RequestBody): Call<Books>

    @DELETE("books/{id}")
    fun deleteBooks(@Path("id") bookId: String): Call<Books>
}