package com.fuh.photosapp.utils.extensions

import android.app.Activity
import android.view.View

/**
 * Created by Nikita on 23.04.18.
 */
fun Activity.stabilizeLayout() {
    window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
}