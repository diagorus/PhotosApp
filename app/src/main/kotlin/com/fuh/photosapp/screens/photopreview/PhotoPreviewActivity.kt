package com.fuh.photosapp.screens.photopreview

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.FileProvider
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.fuh.photosapp.R
import com.fuh.photosapp.data.Photo
import com.fuh.photosapp.utils.FileSaver
import com.fuh.photosapp.utils.base.BaseActivity
import com.fuh.photosapp.utils.extensions.initBasicToolbar
import com.fuh.photosapp.utils.extensions.makeGone
import com.fuh.photosapp.utils.extensions.makeVisible
import com.fuh.photosapp.utils.extensions.subscribeOnMain
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_photopreview.*
import org.jetbrains.anko.share
import org.jetbrains.anko.toast
import java.io.File

/**
 * Created by Nikita on 20.04.18.
 */
class PhotoPreviewActivity : BaseActivity(), PhotoPreviewContract.View {

    companion object {

        private const val EXTRA_PHOTO = "com.fuh.photosapp.EXTRA_PHOTO"

        fun newIntent(from: Context, photo: Photo): Intent {
            return Intent(from, PhotoPreviewActivity::class.java)
                    .apply {
                        putExtra(EXTRA_PHOTO, photo)
                    }
        }

        fun start(from: Context, photo: Photo) {
            val intent = newIntent(from, photo)

            from.startActivity(intent)
        }

        fun startWithTransition(from: Activity, sharedElement: View, photo: Photo) {
            val intent = newIntent(from, photo)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    from,
                    sharedElement,
                    "preview"
            )
                    .toBundle()


            from.startActivity(intent, options)
        }
    }

    override val layoutRes: Int = R.layout.activity_photopreview

    override lateinit var presenter: PhotoPreviewContract.Presenter

    private lateinit var photo: Photo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retrievePhoto()

        presenter = PhotoPreviewPresenter(this, FileSaver(this))
        presenter.start()


        initToolbar()
        initViews()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_photopreview, menu)

        return true
    }


    override fun onDestroy() {
        super.onDestroy()

        presenter.stop()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                supportFinishAfterTransition()

                true
            }
            R.id.itmPhotoPreviewDownload -> {
                downloadPhoto()

                true
            }
            R.id.itmPhotoPreviewShare -> {
                share(photo.urls?.full!!, getString(R.string.share_subject))

                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    override fun showPhotoDownloadingStarted() {
        toast(getString(R.string.downloading_started))
    }

    override fun showPhotoDownloadingError() {
        toast(getString(R.string.downloading_error))
    }

    override fun showPhotoDownloadingFinished(file: File) {
        Snackbar
                .make(clPhotoPreviewContainer, getString(R.string.photo_downloaded), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.open)) {
                    val intent = Intent()
                            .apply {
                                action = Intent.ACTION_VIEW
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                                val photoUri = FileProvider.getUriForFile(
                                        this@PhotoPreviewActivity,
                                        getString(R.string.file_provider_authorities),
                                        file
                                )
                                setDataAndType(photoUri, "image/*")
                            }
                    startActivity(intent)
                }
                .show()
    }

    private fun initToolbar() {
        initBasicToolbar({
            title = ""
        }, {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
        })
    }

    private fun initViews() {
        pbPhotoPreviewLoading.makeVisible()

        Glide.with(this)
                .load(photo.urls?.full)
                .thumbnail(Glide.with(this).load(photo.urls?.thumb))
                .listener(object : RequestListener<Drawable> {
                    override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                        pbPhotoPreviewLoading.makeGone()

                        return false
                    }

                    override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                        //do nothing

                        return false
                    }
                })
                .into(pvPhotoPreviewPhoto)
        pvPhotoPreviewPhoto.setOnPhotoTapListener { _, _, _ ->
            toggleUiVisibility()
        }
    }

    private fun downloadPhoto() {
        RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribeOnMain {
                    if (it) {
                        presenter.downloadPhoto(photo)
                    }
                }
    }

    private fun toggleUiVisibility() {
        if (window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_IMMERSIVE == View.SYSTEM_UI_FLAG_IMMERSIVE) {
            showSystemUI()
        } else {
            hideSystemUI()
        }
    }

    private fun showSystemUI() {
        window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        supportActionBar?.show()
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_IMMERSIVE
        supportActionBar?.hide()
    }

    private fun retrievePhoto() {
        photo = intent.getParcelableExtra(EXTRA_PHOTO)
                ?: throw IllegalArgumentException("EXTRA_PHOTO wasnt supplied!")
    }
}