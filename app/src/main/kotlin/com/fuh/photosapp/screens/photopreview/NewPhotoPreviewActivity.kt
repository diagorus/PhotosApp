package com.fuh.photosapp.screens.photopreview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.fuh.photosapp.R
import com.fuh.photosapp.data.Photo
import com.fuh.photosapp.utils.base.BaseActivity
import com.fuh.photosapp.utils.extensions.toArrayList

/**
 * Created by Nikita on 24.04.18.
 */
class NewPhotoPreviewActivity : BaseActivity() {

    companion object {

        private const val EXTRA_INITIAL_PHOTOS = "com.fuh.photosapp.EXTRA_INITIAL_PHOTOS"

        fun start(from: Context, initialPhotos: List<Photo>) {
            val intent = Intent(from, NewPhotoPreviewActivity::class.java).apply {
                putExtra(EXTRA_INITIAL_PHOTOS, initialPhotos.toArrayList())
            }

            from.startActivity(intent)
        }
    }

    override val layoutRes: Int = R.layout.activity_photo_preview_new

    private lateinit var initialPhotos: List<Photo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    private fun retrieveInitialPhotos() {
    }
}