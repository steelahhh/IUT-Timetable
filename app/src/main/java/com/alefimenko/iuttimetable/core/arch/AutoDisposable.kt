package com.alefimenko.iuttimetable.core.arch

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/*
 * Created by Alexander Efimenko on 26/11/18.
 */

class AutoDisposable : LifecycleObserver {
    private lateinit var compositeDisposable: CompositeDisposable

    fun bindTo(lifecycle: Lifecycle) {
        lifecycle.addObserver(this)
        compositeDisposable = CompositeDisposable()
    }

    fun add(disposable: Disposable) {
        if (::compositeDisposable.isInitialized) {
            compositeDisposable.add(disposable)
        } else {
            throw NotImplementedError("must bind AutoDisposable to a Lifecycle first")
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        compositeDisposable.dispose()
    }
}
