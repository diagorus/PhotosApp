package com.fuh.photosapp.screens.photopreview

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import com.fuh.photosapp.R
import com.fuh.photosapp.data.Photo
import com.fuh.photosapp.utils.extensions.inflateAsChild


/**
 * Created by Nikita on 24.04.18.
 */
class GalleryPagerAdapter(private val ctx: Context, initialItems: List<Photo> = listOf()) : PagerAdapter() {

    var items: List<Photo> = initialItems

    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val itemView = collection.inflateAsChild(R.layout.item_photo_gallery)
        collection.addView(itemView)
        return itemView
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
    }
    override fun getCount(): Int {
        return items.count()
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }
}