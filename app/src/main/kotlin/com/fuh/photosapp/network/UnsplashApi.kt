package com.fuh.photosapp.network

import com.fuh.photosapp.data.Photo
import com.fuh.photosapp.data.PhotosSearchResponse
import com.fuh.photosapp.data.PhotosSortOrder
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * Created by Nikita on 19.04.18.
 */
interface UnsplashApi {

    @GET("photos")
    fun loadPhotos(
            @Query("page") pageNumber: Int? = null,
            @Query("per_page") quantityOnPage: Int? = null,
            @Query("order_by") orderBy: PhotosSortOrder? = null
    ): Single<List<Photo>>

    @GET("search/photos")
    fun searchPhotos(
            @Query("query") query: String,
            @Query("page") pageNumber: Int? = null,
            @Query("per_page") quantityOnPage: Int? = null
    ): Single<PhotosSearchResponse>

    @GET
    fun download(@Url url: String): Single<ResponseBody>
}