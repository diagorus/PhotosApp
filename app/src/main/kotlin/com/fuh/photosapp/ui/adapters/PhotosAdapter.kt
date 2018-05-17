package com.fuh.photosapp.ui.adapters

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fuh.photosapp.R
import com.fuh.photosapp.data.Photo
import com.fuh.photosapp.ui.PhotosDiffCallback
import com.fuh.photosapp.utils.extensions.getNullObject
import com.fuh.photosapp.utils.extensions.inflateAsChild
import com.fuh.photosapp.utils.extensions.isNullObject
import kotlinx.android.synthetic.main.item_photo.view.*
import kotlin.properties.Delegates

/**
 * Created by Nikita on 19.04.18.
 */
class PhotosAdapter(
        initialItems: List<Photo> = listOf()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onPhotoItemClick: (View, Photo) -> Unit = { _, _ -> }

    var items: List<Photo> by Delegates.observable(initialItems) { _, old, new ->
        val diffResult = DiffUtil.calculateDiff(PhotosDiffCallback(old, new))
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            PhotoViewHolder.ID -> {
                PhotoViewHolder.create(parent).apply {
                    onClick = onPhotoItemClick
                }
            }
            ProgressViewHolder.ID -> {
                ProgressViewHolder.create(parent)
            }
            else -> throw IllegalArgumentException("Illegal viewType!")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PhotoViewHolder -> {
                val item = items[position]
                holder.bind(item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val data = items[position]

        return when {
            data.isNullObject() -> ProgressViewHolder.ID
            else -> PhotoViewHolder.ID
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private var loadingPosition = -1

    fun showLoading() {
        if (loadingPosition == -1) {
            loadingPosition = items.size

            items = items.toMutableList().apply { add(loadingPosition, Photo.getNullObject()) }
        }

    }

    fun hideLoading() {
        if (loadingPosition != -1) {
            items = items.toMutableList().apply { removeAt(loadingPosition) }

            loadingPosition = -1
        }
    }

    class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var onClick: (View, Photo) -> Unit = { _, _ -> }

        companion object {
            const val ID: Int = R.layout.item_photo

            fun create(parent: ViewGroup): PhotoViewHolder {
                val itemView = parent.inflateAsChild(R.layout.item_photo)

                return PhotoViewHolder(itemView)
            }
        }

        fun bind(data: Photo) {
            with(itemView) {
                Glide.with(this)
                        .load(data.urls?.thumb)
                        .apply(RequestOptions().placeholder(R.drawable.ic_image))
                        .into(ivPhotoItemPhoto)
                setOnClickListener { onClick(this, data) }
            }
        }
    }

    class ProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        companion object {
            const val ID: Int = R.layout.item_progress

            fun create(parent: ViewGroup): ProgressViewHolder {
                val itemView = parent.inflateAsChild(R.layout.item_progress)

                return ProgressViewHolder(itemView)
            }
        }
    }
}