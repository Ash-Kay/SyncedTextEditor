package com.example.syncedtexteditor.domain.usercase

import com.example.syncedtexteditor.domain.interfaces.Repository
import com.example.syncedtexteditor.model.Note
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainUsecase @Inject constructor(
    val repository: Repository
) {

    fun getAllNotes(): Single<Note> {
        return repository.getAllNotes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun postOrUpdateNote(title: String, body: String): Completable {
        return repository.postOrUpdateNote(title, body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}