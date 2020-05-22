package com.example.maapp.presentation.ui.base.view_model

import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import java.util.*

class EventsQueue : MutableLiveData<Queue<Event>>() {

    @MainThread
    fun offer(event: Event) {
        val queue = (value ?: LinkedList()).apply {
            add(event)
        }

        value = queue
    }
}
