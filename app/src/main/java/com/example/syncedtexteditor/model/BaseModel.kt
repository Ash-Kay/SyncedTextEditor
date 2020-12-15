package com.example.syncedtexteditor.model

import com.google.gson.annotations.SerializedName

data class BaseModel<T>(
    @SerializedName("success")
    val isSuccess: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: T,

    @SerializedName("errorCode")
    val errorCode: String?
)