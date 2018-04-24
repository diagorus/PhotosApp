package com.fuh.photosapp.data

import com.google.gson.annotations.SerializedName

data class PhotosSearchResponse(
        @SerializedName("total")
        var total: Int? = null,
        @SerializedName("total_pages")
        var totalPages: Int? = null,
        @SerializedName("results")
        var results: List<Photo>? = null
)
