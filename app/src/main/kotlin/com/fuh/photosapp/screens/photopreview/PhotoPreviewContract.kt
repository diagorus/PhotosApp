package com.fuh.photosapp.screens.photopreview

import com.fuh.photosapp.data.Photo
import com.fuh.photosapp.utils.mvp.BasePresenter
import com.fuh.photosapp.utils.mvp.BaseView
import java.io.File

/**
 * Created by Nikita on 21.04.18.
 */
object PhotoPreviewContract {

    interface Presenter : BasePresenter {
        fun downloadPhoto(photo: Photo)
    }

    interface View : BaseView<Presenter> {
        fun showPhotoDownloadingStarted()
        fun showPhotoDownloadingError()
        fun showPhotoDownloadingFinished(file: File)
    }
}