package com.example.syncedtexteditor.remote

import com.example.syncedtexteditor.domain.interfaces.Repository
import com.example.syncedtexteditor.model.Note
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class RemoteDataImpl @Inject constructor(
    private val retrofitService: RetrofitService
) : Repository {
    override fun getAllNotes(): Single<Note> {
        return retrofitService.getAllNote().map { baseModel ->
            println(baseModel)
            return@map baseModel.data.first()
        }
    }

    override fun postOrUpdateNote(title: String, body: String): Completable {
        return retrofitService.postOrUpdateNote(title, body)
    }
}