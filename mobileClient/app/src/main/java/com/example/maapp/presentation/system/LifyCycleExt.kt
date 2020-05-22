package com.example.maapp.presentation.system

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.example.maapp.presentation.ui.base.view_model.Event
import com.example.maapp.presentation.ui.base.view_model.EventsQueue
import java.util.*
import javax.inject.Provider

fun <T> MutableLiveData<T>.onNext(next: T) {
    this.value = next
}

inline fun <reified VM : ViewModel> Fragment.getViewModel(provider: Provider<VM>): VM {
    val factory = viewModelFactory {
        provider.get()
    }
    return this.obtainViewModel(factory)
}

inline fun <VM : ViewModel> viewModelFactory(crossinline f: () -> VM): ViewModelProvider.Factory {
    return object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(aClass: Class<T>): T = f() as T
    }
}

inline fun <reified T : ViewModel> FragmentActivity.obtainViewModel(
        viewModelFactory: ViewModelProvider.Factory? = null
): T {
    return ViewModelProviders.of(this, viewModelFactory)[T::class.java]
}

inline fun <reified T : ViewModel> FragmentActivity.obtainViewModel(
        viewModelFactory: ViewModelProvider.Factory,
        body: T.() -> Unit
): T {
    val vm = ViewModelProviders.of(this, viewModelFactory)[T::class.java]
    vm.body()
    return vm
}

inline fun <reified T : ViewModel> Fragment.obtainViewModel(
        viewModelFactory: ViewModelProvider.Factory? = null
): T {
    return ViewModelProviders.of(this, viewModelFactory)[T::class.java]
}

inline fun <reified T : ViewModel> Fragment.obtainViewModel(
        viewModelFactory: ViewModelProvider.Factory,
        body: T.() -> Unit
): T {
    val vm = ViewModelProviders.of(this, viewModelFactory)[T::class.java]
    vm.body()
    return vm
}

inline fun <reified T : Any, reified L : LiveData<T>> Fragment.observe(
        liveData: L,
        noinline block: (T) -> Unit
) {
    liveData.observe(viewLifecycleOwner, Observer<T> { it?.let { block.invoke(it) } })
}

/**
 * Подписка на live data с очередью одноразовых event
 * Например, показы snackbar, диалогов
 */
fun Fragment.observe(eventsQueue: EventsQueue, eventHandler: (Event) -> Unit) {
    eventsQueue.observe(
            this.viewLifecycleOwner,
            Observer<Queue<Event>> { queue: Queue<Event>? ->
                while (queue != null && queue.isNotEmpty()) {
                    eventHandler(queue.poll())
                }
            }
    )
}