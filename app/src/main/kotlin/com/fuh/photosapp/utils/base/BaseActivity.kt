package com.fuh.photosapp.utils.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    @get:LayoutRes
    protected abstract val layoutRes: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(layoutRes)
    }
}
