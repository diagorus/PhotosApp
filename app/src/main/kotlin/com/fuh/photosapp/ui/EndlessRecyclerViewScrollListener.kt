package com.fuh.photosapp.ui

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager

abstract class EndlessRecyclerViewScrollListener(params: Params) : RecyclerView.OnScrollListener() {

    // Quantity of special items that should be ignored when calculating
    // moment of loading
    private var specialItemCount = params.specialItemCount
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private var visibleThreshold = params.visibleThreshold
    // Sets the starting page index
    private var startingPageIndex = params.startingPageIndex
    // The current offset index of data you have loaded
    private var currentPage = startingPageIndex
    // The total number of items in the dataset after the last load
    private var previousTotalItemCount = params.initialItemsCount

    // True if we are still waiting for the last set of data to load.
    private var loading = true

    private lateinit var layoutManager: RecyclerView.LayoutManager

    constructor(layoutManager: LinearLayoutManager, params: Params) : this(params) {
        this.layoutManager = layoutManager
    }

    constructor(layoutManager: GridLayoutManager, params: Params) : this(params) {
        this.layoutManager = layoutManager

        visibleThreshold *= layoutManager.spanCount
    }

    constructor(layoutManager: StaggeredGridLayoutManager, params: Params) : this(params) {
        this.layoutManager = layoutManager

        visibleThreshold *= layoutManager.spanCount
    }

    private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }

    // This happens many times a second during a scroll, so be wary of the code you place here.
    // We are given a few useful parameters to help us work out if we need to load some more data,
    // but first we check if we are waiting for the previous load to finish.
    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        var lastVisibleItemPosition = 0
        val totalItemCount = layoutManager.itemCount

        if (layoutManager is StaggeredGridLayoutManager) {
            val lastVisibleItemPositions = (layoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(null)
            // get maximum element within the list
            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions) - specialItemCount
        } else if (layoutManager is GridLayoutManager) {
            lastVisibleItemPosition = (layoutManager as GridLayoutManager).findLastVisibleItemPosition() - specialItemCount
        } else if (layoutManager is LinearLayoutManager) {
            lastVisibleItemPosition = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition() - specialItemCount
        }

        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex
            this.previousTotalItemCount = totalItemCount
            if (totalItemCount == 0) {
                this.loading = true
            }
        }
        // If it’s still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        val totalItemCountWithoutSpecials = totalItemCount - specialItemCount
        if (loading && totalItemCountWithoutSpecials > previousTotalItemCount) {
            loading = false
            previousTotalItemCount = totalItemCountWithoutSpecials
        }

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        // threshold should reflect how many total columns there are too
        if (!loading && lastVisibleItemPosition + visibleThreshold > totalItemCount) {
            currentPage++
            onLoadMore(currentPage, totalItemCount, view)
            loading = true
        }
    }

    // Call this method whenever performing new searches
    fun resetState() {
        this.currentPage = this.startingPageIndex
        this.previousTotalItemCount = 0
        this.loading = true
    }

    // Defines the process for actually loading more data based on page
    abstract fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView)

    data class Params(
            var specialItemCount: Int = 0,
            var visibleThreshold: Int = 5,
            var initialItemsCount: Int = 0,
            var startingPageIndex: Int = 0
    )
}