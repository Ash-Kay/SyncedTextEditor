package com.example.syncedtexteditor.ui

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.syncedtexteditor.domain.usercase.MainUsecase
import com.example.syncedtexteditor.model.Note
import com.example.syncedtexteditor.utils.SharedPreferences
import com.example.syncedtexteditor.utils.Status
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import java.net.ConnectException

class MainViewModel @ViewModelInject constructor(
    private val mainUsecase: MainUsecase,
    val application: Application
) : ViewModel() {

    private val composite = CompositeDisposable()

    val note: MutableLiveData<Note> = MutableLiveData(Note(title = "", body = ""))
    var status: MutableLiveData<Status> = MutableLiveData(Status.INITIAL)

    val TITLE_KEY = "TITLE_KEY"
    val BODY_KEY = "BODY_KEY"

    fun fetchNote() {
        status.value = Status.FETCHING
        composite.add(
            mainUsecase.getAllNotes().subscribe(
                {
                    Timber.d("Notes Fetched $it")
                    note.value = it
                    status.value = Status.IDLE

                    SharedPreferences.saveData(
                        context = application,
                        key = TITLE_KEY,
                        value = it.title
                    )
                    SharedPreferences.saveData(
                        context = application,
                        key = BODY_KEY,
                        value = it.body
                    )
                },
                {
                    when (it) {
                        is ConnectException -> {
                            status.value = Status.OFFLINE

                            val title = SharedPreferences.getStringData(
                                context = application,
                                key = TITLE_KEY
                            )

                            val body = SharedPreferences.getStringData(
                                context = application,
                                key = BODY_KEY
                            )

                            note.value = Note(title = title.orEmpty(), body = body.orEmpty())
                        }
                        else -> {
                            Timber.e(it)
                            status.value = Status.ERROR
                        }
                    }
                })
        )
    }

    fun postOrUpdateNote() {
        status.value = Status.UPLOADING
        composite.add(
            mainUsecase.postOrUpdateNote(
                note.value?.title.orEmpty(),
                note.value?.body.orEmpty()
            )
                .subscribe(
                    {
                        Timber.d("Pushed to Server Successfully")
                        status.value = Status.IDLE
                    }, {
                        when (it) {
                            is ConnectException -> {
                                status.value = Status.OFFLINE

                                SharedPreferences.saveData(
                                    context = application,
                                    key = TITLE_KEY,
                                    value = note.value?.title.orEmpty()
                                )
                                SharedPreferences.saveData(
                                    context = application,
                                    key = BODY_KEY,
                                    value = note.value?.body.orEmpty()
                                )
                            }
                            else -> {
                                Timber.e(it)
                                status.value = Status.ERROR
                            }
                        }
                    }
                ))
    }

    fun setNoteTitle(title: String) {
        note.value = note.value?.copy(title = title)
    }

    fun setNoteBody(body: String) {
        note.value = note.value?.copy(body = body)
    }

    override fun onCleared() {
        super.onCleared()
        composite.dispose()
    }
}