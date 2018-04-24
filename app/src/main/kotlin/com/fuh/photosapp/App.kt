package com.fuh.photosapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import android.support.v7.app.AppCompatDelegate
import com.facebook.stetho.Stetho
import com.fuh.photosapp.network.NetApiClient
import com.jakewharton.threetenabp.AndroidThreeTen
import timber.log.Timber

class App : MultiDexApplication() {
    companion object {
        const val NOTIFICATION_CHANNEL_ID = "channel_01"
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            initStetho()
            initTimber()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupNotificationChannel()
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
        Stetho.initializeWithDefaults(this)
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupNotificationChannel() {
        val name = getString(R.string.app_name)
        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, NotificationManager.IMPORTANCE_LOW)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}