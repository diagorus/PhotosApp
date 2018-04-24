package com.fuh.photosapp.network

import android.content.Context
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.fuh.photosapp.data.Photo
import com.fuh.photosapp.data.PhotosSearchResponse
import com.fuh.photosapp.data.PhotosSortOrder
import com.google.gson.GsonBuilder
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Url
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Created by Nikita on 19.04.18.
 */
object NetApiClient {

    private const val accessKey: String = "1709d994b297a86d704df5c79df0f3827815dae4a87ce7abf88e6e798103184f"

    private lateinit var netApi: UnsplashApi

    fun init(appCtx: Context) {
        val loggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { Timber.i(it) })
                .apply { level = HttpLoggingInterceptor.Level.BODY }

        val tokenInterceptor = AuthorizationHeaderInterceptor(accessKey)

        val httpCache = Cache(appCtx.cacheDir, 10 * 1024 * 1024)

        val httpClient = OkHttpClient.Builder()
                .addInterceptor(tokenInterceptor)
                .addInterceptor(loggingInterceptor)
                .addNetworkInterceptor(StethoInterceptor())
                .cache(httpCache)
                .connectTimeout(40, TimeUnit.SECONDS)
                .readTimeout(40, TimeUnit.SECONDS)
                .writeTimeout(40, TimeUnit.SECONDS)
                .build()

        val gson = GsonBuilder().create()
        val gsonConverterFactory = GsonConverterFactory.create(gson)

        val rxAdapterFactory = RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())

        val baseUrl = "https://api.unsplash.com/"
        val builder = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxAdapterFactory)
                .client(httpClient)
                .build()

        netApi = builder.create(UnsplashApi::class.java)
    }

    fun loadPhotos(
            pageNumber: Int? = null,
            quantityOnPage: Int? = null,
            orderBy: PhotosSortOrder? = null
    ): Single<List<Photo>> =
            netApi.loadPhotos(pageNumber, quantityOnPage, orderBy)

    fun searchPhotos(
            query: String,
            pageNumber: Int? = null,
            quantityOnPage: Int? = null
    ): Single<PhotosSearchResponse> =
            netApi.searchPhotos(query, pageNumber, quantityOnPage)

    fun download(@Url url: String): Single<ResponseBody> =
            netApi.download(url)
}