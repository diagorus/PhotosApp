package com.fuh.photosapp.utils.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseFragment : Fragment() {

    @get:LayoutRes
    protected abstract val layoutRes: Int

    protected lateinit var rootView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(layoutRes, container, false)
        return rootView
    }
}
