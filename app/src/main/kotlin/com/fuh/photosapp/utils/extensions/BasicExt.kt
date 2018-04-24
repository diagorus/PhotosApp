package com.fuh.photosapp.utils.extensions

inline fun <T> T.takeIfOrDefault(default: T, predicate: (T) -> Boolean): T {
    return takeIf(predicate) ?: default
}

fun isAllNotNull(vararg objects: Any?): Boolean {
    return objects.all { it != null }
}