package com.fuh.photosapp.data

import com.google.gson.annotations.SerializedName

enum class PhotosSortOrder {
    @SerializedName("latest")
    LATEST,
    @SerializedName("oldest")
    OLDEST,
    @SerializedName("popular")
    POPULAR
}
