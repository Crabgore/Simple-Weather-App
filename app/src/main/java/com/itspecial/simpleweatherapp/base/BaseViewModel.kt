package com.itspecial.simpleweatherapp.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel : ViewModel() {
    private val compositeDisposable by lazy { CompositeDisposable() }

    val failure: MutableLiveData<Throwable> = MutableLiveData()

    fun addDisposable(disposable: Disposable) = compositeDisposable.add(disposable)

    protected fun handleFailure(failure: Throwable) {
        this.failure.value = failure
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }
}