package com.example.syncedtexteditor.remote

import com.example.syncedtexteditor.model.BaseModel
import com.example.syncedtexteditor.model.Note
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface RetrofitService {
    @GET("/api/v1/notes")
    fun getAllNote(): Single<BaseModel<List<Note>>>

    @FormUrlEncoded
    @POST("/api/v1/notes")
    fun postOrUpdateNote(@Field("title") title: String, @Field("body") body: String): Completable
}