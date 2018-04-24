package com.fuh.photosapp.utils.extensions

import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import kotlinx.android.synthetic.main.include_toolbar_basic.*

fun AppCompatActivity.initBasicToolbar(
        toolbarInitBlock: Toolbar.() -> Unit = {},
        actionBarInitBlock: ActionBar.() -> Unit = {}
) {
    toolbar?.let {
        setSupportActionBar(it)
    } ?: throw IllegalStateException("Activity must have Toolbar with id: 'toolbar'")

    toolbar.toolbarInitBlock()
    supportActionBar?.actionBarInitBlock()
}