package com.fuh.photosapp.screens.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.Menu
import com.fuh.photosapp.R
import com.fuh.photosapp.data.Photo
import com.fuh.photosapp.screens.photopreview.PhotoPreviewActivity
import com.fuh.photosapp.ui.EndlessRecyclerViewScrollListener
import com.fuh.photosapp.ui.adapters.PhotosAdapter
import com.fuh.photosapp.utils.base.BaseActivity
import com.fuh.photosapp.utils.extensions.*
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.activity_search.*
import java.util.concurrent.TimeUnit

/**
 * Created by Nikita on 23.04.18.
 */
class SearchActivity : BaseActivity(), SearchContract.View {

    companion object {

        fun start(from: Context) {
            val intent = Intent(from, SearchActivity::class.java)

            from.startActivity(intent)
        }
    }

    override val layoutRes: Int = R.layout.activity_search

    override lateinit var presenter: SearchContract.Presenter

    private lateinit var paginationScrollListener: EndlessRecyclerViewScrollListener
    private lateinit var searchPhotosAdapter: PhotosAdapter

    private var searchDisposables: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stabilizeLayout()

        initToolbar()
        initViews()

        presenter = SearchPresenter(this)
        presenter.start()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)

        val searchView = menu.findItem(R.id.itmSearchSearch).actionView as SearchView
        initSearchView(searchView)

        return true
    }

    override fun onDestroy() {
        super.onDestroy()

        searchDisposables.dispose()
        presenter.stop()
    }

    override fun showInitialFoundPhotos(data: List<Photo>) {
        paginationScrollListener.resetState()
        searchPhotosAdapter.items = data
    }

    override fun showFoundMoreFoundPhotos(data: List<Photo>) {
        searchPhotosAdapter.items = searchPhotosAdapter.items
                .toMutableList()
                .apply { addAll(data) }
    }

    override fun hideWelcome() {
        rlSearchWelcome.makeGone()
    }

    override fun showNotingFound() {
        rlSearchEmptyResults.makeVisible()
    }

    override fun hideNotingFound() {
        rlSearchEmptyResults.makeGone()
    }

    override fun showProgress() {
        searchPhotosAdapter.showLoading()
    }

    override fun hideProgress() {
        searchPhotosAdapter.hideLoading()
    }

    private fun initSearchView(searchView: SearchView) {
        searchView.isIconified = false
        searchView.queryHint = getString(R.string.search_hint)

        val textChangeEventsObs =
                RxSearchView.queryTextChangeEvents(searchView)
                        .publish()

        searchDisposables +=
                textChangeEventsObs
                        .map { it.queryText().toString() }
                        .filter { it.trim().isNotBlank() }
                        .distinctUntilChanged()
                        .debounce(350, TimeUnit.MILLISECONDS)
                        .subscribeOnMain { query ->
                            if (searchPhotosAdapter.items.isNotEmpty()) {
                                searchPhotosAdapter.items = emptyList()
                            }
                            presenter.searchPhotos(query)
                        }

        searchDisposables +=
                textChangeEventsObs
                        .filter { it.isSubmitted }
                        .subscribeOnMain {
                            it.view().clearFocus()
                            hideKeyboard()
                        }

        searchDisposables +=
                textChangeEventsObs.connect()

        searchView.setOnCloseListener {
            finish()

            true
        }
    }

    private fun initViews() {
        searchPhotosAdapter = PhotosAdapter().apply {
            onPhotoItemClick = { view, photo ->
                PhotoPreviewActivity.startWithTransition(this@SearchActivity, view, photo)
            }
        }
        rvSearchPhotos.adapter = searchPhotosAdapter

        val photosLayoutManager = GridLayoutManager(this, 3)
                .apply {
                    spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                        override fun getSpanSize(position: Int): Int {
                            val viewType = searchPhotosAdapter.getItemViewType(position)

                            return when (viewType) {
                                PhotosAdapter.PhotoViewHolder.ID -> 1
                                PhotosAdapter.ProgressViewHolder.ID -> 3
                                else -> throw IllegalArgumentException("Unknown viewType!")
                            }
                        }
                    }
                }
        rvSearchPhotos.layoutManager = photosLayoutManager

        paginationScrollListener =
                object : EndlessRecyclerViewScrollListener(
                        photosLayoutManager,
                        Params(1, 5, 30, 1)
                ) {

                    override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                        presenter.loadMoreSearchResults(page)
                    }
                }
        rvSearchPhotos.addOnScrollListener(paginationScrollListener)
    }

    private fun initToolbar() {
        initBasicToolbar({
            setNavigationOnClickListener { finish() }
        }, {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.search)
        })
    }
}