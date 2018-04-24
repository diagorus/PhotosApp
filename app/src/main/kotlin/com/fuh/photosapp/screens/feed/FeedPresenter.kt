package com.fuh.photosapp.screens.feed

import com.fuh.photosapp.data.Photo
import com.fuh.photosapp.data.PhotosSortOrder
import com.fuh.photosapp.network.NetApiClient
import com.fuh.photosapp.utils.extensions.subscribeOnMain
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

/**
 * Created by Nikita on 22.04.18.
 */
class FeedPresenter(private val view: FeedContract.View) : FeedContract.Presenter {

    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun start() {
        loadFeedPhotos()
    }

    override fun stop() {
        disposables.clear()
    }

    override fun loadFeedPhotos() {
        disposables.clear()
        disposables +=
                loadPhotos(1)
                        .subscribeOnMain {
                            view.showFeedPhotos(it)
                        }
    }

    override fun loadMoreFeedPhotos(pageNumber: Int) {
        disposables.clear()
        disposables +=
                loadPhotos(pageNumber)
                        .subscribeOnMain {
                            view.showFeedPhotos(it)
                        }
    }

    private fun loadPhotos(pageNumber: Int): Single<List<Photo>> {
        return NetApiClient.loadPhotos(pageNumber, 30, PhotosSortOrder.POPULAR)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { view.showProgress() }
                .doFinally { view.hideProgress() }
    }
}