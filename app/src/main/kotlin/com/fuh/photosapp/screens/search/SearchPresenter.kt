package com.fuh.photosapp.screens.search

import com.fuh.photosapp.data.Photo
import com.fuh.photosapp.network.NetApiClient
import com.fuh.photosapp.utils.extensions.subscribeOnMain
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

/**
 * Created by Nikita on 22.04.18.
 */
class SearchPresenter(private val view: SearchContract.View) : SearchContract.Presenter {

    private val disposables: CompositeDisposable = CompositeDisposable()

    private var lastSearchQuery: String = ""

    override fun stop() {
        disposables.clear()
    }

    override fun searchPhotos(query: String) {
        lastSearchQuery = query
        disposables.clear()
        disposables +=
                searchPhotos(query, 1)
                        .subscribeOnMain {
                            view.showInitialFoundPhotos(it)

                            if (it.count() == 0) {
                                view.showNotingFound()
                            }
                        }
    }

    override fun loadMoreSearchResults(pageNumber: Int) {
        disposables.clear()
        disposables +=
                searchPhotos(lastSearchQuery, pageNumber)
                        .subscribeOnMain {
                            view.showFoundMoreFoundPhotos(it)
                        }
    }

    private fun searchPhotos(query: String, pageNumber: Int): Single<List<Photo>> =
            NetApiClient.searchPhotos(query, pageNumber, 30)
                    .map { it.results!! }
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        view.hideWelcome()
                        view.hideNotingFound()
                        view.showProgress()
                    }
                    .doFinally { view.hideProgress() }
}