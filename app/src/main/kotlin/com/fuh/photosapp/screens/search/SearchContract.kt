package com.fuh.photosapp.screens.search

import com.fuh.photosapp.data.Photo
import com.fuh.photosapp.utils.mvp.BasePresenter
import com.fuh.photosapp.utils.mvp.BaseView

/**
 * Created by Nikita on 22.04.18.
 */
object SearchContract {

    interface Presenter : BasePresenter {
        fun searchPhotos(query: String)
        fun loadMoreSearchResults(pageNumber: Int)
    }

    interface View : BaseView<Presenter> {
        fun showInitialFoundPhotos(data: List<Photo>)
        fun showFoundMoreFoundPhotos(data: List<Photo>)
        fun showProgress()
        fun hideProgress()
        fun hideWelcome()
        fun showNotingFound()
        fun hideNotingFound()
    }
}