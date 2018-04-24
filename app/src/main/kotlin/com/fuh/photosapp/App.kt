package com.fuh.photosapp

import android.content.Context
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import android.support.v7.app.AppCompatDelegate
import com.facebook.stetho.Stetho
import com.fuh.photosapp.network.NetApiClient
import com.jakewharton.threetenabp.AndroidThreeTen
import timber.log.Timber

class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            initStetho()
            initTimber()
        }

        initVectorCompat()
        initThreeTen()

        NetApiClient.init(applicationContext)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)

        MultiDex.install(this)
    }

    private fun initVectorCompat() {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    private fun initThreeTen() {
        AndroidThreeTen.init(this)
    }

    private fun initStetho() {
        Stetho.initializeWithDefaults(this);
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }
}