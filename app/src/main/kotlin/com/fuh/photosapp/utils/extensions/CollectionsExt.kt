package com.fuh.photosapp.utils.extensions

/**
 * Created by Nikita on 24.04.18.
 */
fun <E> List<E>.toArrayList(): ArrayList<E> {
    return ArrayList(this)
}