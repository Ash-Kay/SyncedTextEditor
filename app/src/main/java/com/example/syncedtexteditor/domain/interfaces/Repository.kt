package com.example.syncedtexteditor.domain.interfaces

import com.example.syncedtexteditor.model.Note
import io.reactivex.Completable
import io.reactivex.Single

interface Repository {
    fun getAllNotes(): Single<Note>
    fun postOrUpdateNote(title: String, body: String): Completable
}