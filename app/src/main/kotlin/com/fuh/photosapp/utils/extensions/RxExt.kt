package com.fuh.photosapp.utils.extensions

import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.Disposable
import timber.log.Timber

fun <T> Flowable<T>.subscribeOnMain(
        onNext: (T) -> Unit
): Disposable {
    return this
            .observeOn(mainThread())
            .subscribe(onNext, Timber::e, { })
}

fun <T> Flowable<T>.subscribeOnMain(
        onNext: (T) -> Unit,
        onError: (Throwable) -> Unit = Timber::e,
        onComplete: () -> Unit = { }
): Disposable {
    return this
            .observeOn(mainThread())
            .subscribe(onNext, onError, onComplete)
}

fun <T> Observable<T>.subscribeOnMain(
        onNext: (T) -> Unit,
        onError: (Throwable) -> Unit = Timber::e,
        onComplete: () -> Unit = { }
): Disposable {
    return this
            .observeOn(mainThread())
            .subscribe(onNext, onError, onComplete)
}

fun <T> Observable<T>.subscribeOnMain(
        onNext: (T) -> Unit
): Disposable {
    return this
            .observeOn(mainThread())
            .subscribe(onNext, Timber::e, { })
}

fun <T> Observable<T>.subscribeOnMain(): Disposable {
    return this
            .observeOn(mainThread())
            .subscribe({ }, Timber::e, { })
}

fun <T> Single<T>.subscribeOnMain(
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit
): Disposable {
    return this
            .observeOn(mainThread())
            .subscribe(onSuccess, onError)
}

fun <T> Single<T>.subscribeOnMain(
        onSuccess: (T) -> Unit
): Disposable {
    return subscribeOnMain(onSuccess, Timber::e)
}

fun <T> Single<T>.subscribeOnMain(): Disposable {
    return subscribeOnMain({ }, Timber::e)
}

fun Completable.subscribeOnMain(
        onComplete: () -> Unit,
        onError: (Throwable) -> Unit
): Disposable {
    return this
            .observeOn(mainThread())
            .subscribe(onComplete, onError)
}

fun Completable.subscribeOnMain(
        onComplete: () -> Unit = { }
): Disposable {
    return this
            .subscribeOnMain(onComplete, Timber::e)
}

fun <T> Maybe<T>.subscribeOnMain(
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit
): Disposable {
    return this
            .observeOn(mainThread())
            .subscribe(onSuccess, onError)
}

fun <T> Maybe<T>.subscribeOnMain(
        onSuccess: (T) -> Unit
): Disposable {
    return subscribeOnMain(onSuccess, Timber::e)
}

fun Disposable.disposeIfNot() {
    if (!isDisposed) {
        dispose()
    }
}