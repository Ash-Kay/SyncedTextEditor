package com.example.syncedtexteditor.model

import com.google.gson.annotations.SerializedName

data class Note(
    @SerializedName("id")
    val id: Int = 1,

    @SerializedName("title")
    val title: String,

    @SerializedName("body")
    val body: String
)