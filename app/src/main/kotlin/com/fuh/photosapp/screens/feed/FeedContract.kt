package com.fuh.photosapp.screens.feed

import com.fuh.photosapp.data.Photo
import com.fuh.photosapp.utils.mvp.BasePresenter
import com.fuh.photosapp.utils.mvp.BaseView

/**
 * Created by Nikita on 22.04.18.
 */
object FeedContract {

    interface Presenter : BasePresenter {
        fun loadFeedPhotos()
        fun loadMoreFeedPhotos(pageNumber: Int)
    }

    interface View : BaseView<Presenter> {
        fun showFeedPhotos(data: List<Photo>)
        fun showProgress()
        fun hideProgress()
    }
}