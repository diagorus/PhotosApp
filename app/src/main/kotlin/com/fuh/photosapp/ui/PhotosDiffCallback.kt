package com.fuh.photosapp.ui

import android.support.v7.util.DiffUtil
import com.fuh.photosapp.data.Photo

/**
 * Created by Nikita on 22.04.18.
 */
class PhotosDiffCallback(
        private val oldItems: List<Photo>,
        private val newItems: List<Photo>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldItems.count()
    }

    override fun getNewListSize(): Int {
        return newItems.count()
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldData = oldItems[oldItemPosition]
        val newData = newItems[newItemPosition]

        return oldData.id == newData.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldData = oldItems[oldItemPosition]
        val newData = newItems[newItemPosition]

        return (oldData.urls == newData.urls)
    }
}