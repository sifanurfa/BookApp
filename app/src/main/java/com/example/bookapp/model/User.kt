package com.example.bookapp.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("books")
    val `data`: List<Books>
)
