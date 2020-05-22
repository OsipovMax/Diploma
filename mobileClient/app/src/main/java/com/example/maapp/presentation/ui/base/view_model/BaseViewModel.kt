package com.example.maapp.presentation.ui.base.view_model

import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel : ViewModel() {

    /**
     * LiveData для событий, которые должны быть обработаны один раз
     * Например: показы диалогов, снэкбаров с ошибками
     */
    val eventsQueue = EventsQueue()

    private val compositeDisposable by lazy { CompositeDisposable() }

    override fun onCleared() {
        clearSubscriptions()
        super.onCleared()
    }

    fun clearSubscriptions() = compositeDisposable.clear()

    protected fun Disposable.autoDispose(): Disposable {
        compositeDisposable.add(this)
        return this
    }
}

fun Fragment.showSnackMessage(message: String) {
    view?.let {
        val snackbar = Snackbar.make(it, message, Snackbar.LENGTH_LONG)
        val messageTextView =
                snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        messageTextView.setTextColor(android.graphics.Color.WHITE)
        snackbar.show()
    }
}