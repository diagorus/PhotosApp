package com.fuh.photosapp.utils.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.support.annotation.LayoutRes
import android.support.design.widget.TextInputLayout
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

fun Activity.hideKeyboard() {
    currentFocus?.let {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(it.windowToken, 0)
    }
}

fun View.makeVisible() {
    this.visibility = View.VISIBLE
}

fun View.makeInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.makeGone() {
    this.visibility = View.GONE
}

fun View.generateBitmap(): Bitmap {
    val displayMetrics = DisplayMetrics()
    (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
    layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    measure(displayMetrics.widthPixels, displayMetrics.heightPixels)
    layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
    buildDrawingCache()
    val bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)

    try {
        val canvas = Canvas(bitmap)
        draw(canvas)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return bitmap
}

var TextInputLayout.textValue: String
    get() = editText!!.text.toString()
    set(value) {
        editText!!.setText(value)
    }

var EditText.textValue: String
    get() = text.toString()
    set(value) {
        setText(value)
    }

fun ViewGroup.inflateAsChild(@LayoutRes resource: Int, isAttachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(resource, this, isAttachToRoot)
}

fun TextInputLayout.clearError() {
    isErrorEnabled = false
    error = null
}
