package com.fuh.photosapp.utils.base

import com.fuh.photosapp.utils.mvp.BasePresenter
import io.reactivex.disposables.CompositeDisposable

abstract class BaseRxPresenter : BasePresenter {

    val disposables: CompositeDisposable = CompositeDisposable()

    override fun stop() {
        disposables.clear()
    }
}