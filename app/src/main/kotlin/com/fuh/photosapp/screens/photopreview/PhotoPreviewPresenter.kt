package com.fuh.photosapp.screens.photopreview

import com.fuh.photosapp.data.Photo
import com.fuh.photosapp.network.NetApiClient
import com.fuh.photosapp.utils.FileSaver
import com.fuh.photosapp.utils.extensions.subscribeOnMain
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import java.util.*

/**
 * Created by Nikita on 21.04.18.
 */
class PhotoPreviewPresenter(private val view: PhotoPreviewContract.View, private val fileSaver: FileSaver) : PhotoPreviewContract.Presenter {

    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun stop() {
        disposables.clear()
    }

    override fun downloadPhoto(photo: Photo) {
        disposables +=
                NetApiClient.download(photo.urls?.full!!)
                        .map {
                            val photoFileName = UUID.randomUUID().toString()

                            fileSaver.saveFileToPublic(photoFileName, it.bytes())
                        }
                        .doOnSubscribe { view.showPhotoDownloadingStarted() }
                        .subscribeOnMain {
                            view.showPhotoDownloadingFinished(it)
                        }
    }
}