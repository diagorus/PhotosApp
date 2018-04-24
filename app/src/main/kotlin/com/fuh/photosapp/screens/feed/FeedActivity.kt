package com.fuh.photosapp.screens.feed

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import com.fuh.photosapp.R
import com.fuh.photosapp.data.Photo
import com.fuh.photosapp.screens.photopreview.PhotoPreviewActivity
import com.fuh.photosapp.screens.search.SearchActivity
import com.fuh.photosapp.ui.EndlessRecyclerViewScrollListener
import com.fuh.photosapp.ui.adapters.PhotosAdapter
import com.fuh.photosapp.utils.base.BaseActivity
import com.fuh.photosapp.utils.extensions.initBasicToolbar
import com.fuh.photosapp.utils.extensions.stabilizeLayout
import kotlinx.android.synthetic.main.activity_feed.*

/**
 * Created by Nikita on 19.04.18.
 */
class FeedActivity : BaseActivity(), FeedContract.View {

    companion object {

        fun start(from: Context) {
            val intent = Intent(from, FeedActivity::class.java)

            from.startActivity(intent)
        }
    }

    override val layoutRes: Int = R.layout.activity_feed

    private lateinit var feedPhotosAdapter: PhotosAdapter
    private lateinit var paginationScrollListener: EndlessRecyclerViewScrollListener

    override lateinit var presenter: FeedContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stabilizeLayout()

        initToolbar()
        initViews()

        presenter = FeedPresenter(this)
        presenter.start()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_feed, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itmFeedSearch -> {
                SearchActivity.start(this)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showFeedPhotos(data: List<Photo>) {
        feedPhotosAdapter.items = feedPhotosAdapter.items
                .toMutableList()
                .apply { addAll(data) }
    }

    override fun showProgress() {
        feedPhotosAdapter.showLoading()
    }

    override fun hideProgress() {
        feedPhotosAdapter.hideLoading()
    }

    private fun initToolbar() {
        initBasicToolbar(actionBarInitBlock = {
            title = getString(R.string.feed)
        })
    }

    private fun initViews() {
        feedPhotosAdapter = PhotosAdapter().apply {
            onPhotoItemClick = { view, photo ->
                PhotoPreviewActivity.startWithTransition(this@FeedActivity, view, photo)
            }
        }
        rvFeedPhotos.adapter = feedPhotosAdapter

        val photosLayoutManager = GridLayoutManager(this@FeedActivity, 2)
                .apply {
                    spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                        override fun getSpanSize(position: Int): Int {
                            val viewType = feedPhotosAdapter.getItemViewType(position)

                            return when (viewType) {
                                PhotosAdapter.PhotoViewHolder.ID -> 1
                                PhotosAdapter.ProgressViewHolder.ID -> 2
                                else -> throw IllegalArgumentException("Unknown viewType!")
                            }
                        }
                    }
                }
        rvFeedPhotos.layoutManager = photosLayoutManager
        rvFeedPhotos.setHasFixedSize(true)


        paginationScrollListener =
                object : EndlessRecyclerViewScrollListener(
                        photosLayoutManager,
                        Params(2, 20, 1)
                ) {

                    override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                        presenter.loadMoreFeedPhotos(page)
                    }
                }
        rvFeedPhotos.addOnScrollListener(paginationScrollListener)
    }
}