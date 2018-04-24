package com.fuh.photosapp.network

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by Nikita on 19.04.18.
 */
class AuthorizationHeaderInterceptor(val accessKey: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request()
                .newBuilder()
                .addHeader("Authorization", "Client-ID $accessKey")
                .build()
        return chain.proceed(newRequest)
    }
}