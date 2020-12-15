package com.example.syncedtexteditor.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.example.syncedtexteditor.R
import com.example.syncedtexteditor.domain.usercase.MainUsecase
import com.example.syncedtexteditor.utils.NetworkUtils
import com.example.syncedtexteditor.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var mainUsecase: MainUsecase

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.fetchNote()


        val handler = Handler(Looper.getMainLooper())
        val runnable = Runnable {
            viewModel.postOrUpdateNote()
        }

        etBody.addTextChangedListener {
            if (viewModel.note.value?.body != it.toString()) {
                viewModel.setNoteBody(it.toString())
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, 5000)
            }
        }

        etTitle.addTextChangedListener {
            if (viewModel.note.value?.title != it.toString()) {
                viewModel.setNoteTitle(it.toString())
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, 5000)
            }
        }

        viewModel.status.observe(this, Observer { status ->
            when (status) {
                Status.INITIAL -> tvStatus.text = "Hi"
                Status.FETCHING -> tvStatus.text = "Syncing..."
                Status.UPLOADING -> tvStatus.text = "Saving..."
                Status.IDLE -> {
                    tvStatus.text = "Saved!"

                    etTitle.setText(viewModel.note.value?.title)
                    etBody.setText(viewModel.note.value?.body)
                    etTitle.setSelection(viewModel.note.value?.title?.length ?: 0)
                    etBody.setSelection(viewModel.note.value?.body?.length ?: 0)
                }
                Status.ERROR -> tvStatus.text = "3Rr0R!"
                Status.OFFLINE -> {
                    tvStatus.text = "Offline=/="

                    etTitle.setText(viewModel.note.value?.title)
                    etBody.setText(viewModel.note.value?.body)
                    etTitle.setSelection(viewModel.note.value?.title?.length ?: 0)
                    etBody.setSelection(viewModel.note.value?.body?.length ?: 0)
                }
            }
        })

        NetworkUtils.getNetworkLiveData(this).observe(this, Observer {
            if (it) {
                tvStatus.text = tvStatus.text.toString() + "ONLINE"
                viewModel.postOrUpdateNote()
            } else {
                tvStatus.text = tvStatus.text.toString() + "OFFNLINE"
            }
        })

        viewModel.note.observe(this, Observer {
            etTitle.setText(it.title)
            etBody.setText(it.body)

            etTitle.setSelection(it.title.length)
            etBody.setSelection(it.body.length)
        })
    }
}
