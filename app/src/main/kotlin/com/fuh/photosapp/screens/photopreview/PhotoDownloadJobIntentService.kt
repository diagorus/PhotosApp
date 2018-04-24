package com.fuh.photosapp.screens.photopreview;

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.JobIntentService
import android.support.v4.app.NotificationCompat
import android.support.v4.content.FileProvider
import com.fuh.photosapp.App.Companion.NOTIFICATION_CHANNEL_ID
import com.fuh.photosapp.R
import com.fuh.photosapp.data.Photo
import com.fuh.photosapp.network.NetApiClient
import com.fuh.photosapp.utils.FileSaver
import java.io.File
import java.util.*

class PhotoDownloadJobIntentService : JobIntentService() {

    companion object {
        private const val EXTRA_PHOTO = "com.fuh.photosapp.EXTRA_PHOTO"

        private const val EXTRA_DOWNLOAD_ID = "com.fuh.photosapp.EXTRA_DOWNLOAD_ID"
        private const val EXTRA_FILE_NAME = "com.fuh.photosapp.EXTRA_FILE_NAME"

        private const val JOB_ID = 573

        fun enqueueWork(context: Context, photo: Photo) {
            val intent = Intent().apply {
                putExtra(EXTRA_PHOTO, photo)
            }

            enqueueWork(context, PhotoDownloadJobIntentService::class.java, JOB_ID, intent)
        }
    }

    private val random: Random = Random()

    override fun onHandleWork(intent: Intent) {
        val downloadId = random.nextInt()


        val photo = retrievePhoto(intent)
        val fileName = "${photo.id!!}.png"
        showDownloadingStarted(downloadId, fileName)

        val fileSaver = FileSaver(this)
        try {
            val fileBytes = NetApiClient.download(photo.urls?.full!!).blockingGet().bytes()

            val outputFile = fileSaver.saveFileToPublicDir(fileName, fileBytes)

            showDownloadingCompleted(downloadId, fileName, outputFile)
        } catch (e: Exception) {
            showDownloadingFailed(downloadId, fileName)
        }
    }

    private fun showDownloadingStarted(downloadId: Int, fileName: String) {
        val notification = NotificationCompat.Builder(this)
                .setContentTitle("Downloading...")
                .setContentText(fileName)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setProgress(0, 0, true)
                .setOngoing(true)
                .setChannelId(NOTIFICATION_CHANNEL_ID)
                .build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(downloadId, notification)
    }

    private fun showDownloadingFailed(downloadId: Int, fileName: String) {
        val notification = NotificationCompat.Builder(this)
                .setContentTitle("Downloading failed!")
                .setContentText(fileName)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setProgress(0, 0, false)
                .setAutoCancel(true)
                .setChannelId(NOTIFICATION_CHANNEL_ID)
                .build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(downloadId, notification)
    }

    private fun showDownloadingCompleted(downloadId: Int, fileName: String, file: File) {
        val intentToPhoto = getIntentToPhoto(file)

        val notification = NotificationCompat.Builder(this)
                .setContentTitle("Downloading completed!")
                .setContentText(fileName)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setProgress(0, 0, false)
                .setAutoCancel(true)
                .setContentIntent(intentToPhoto)
                .setChannelId(NOTIFICATION_CHANNEL_ID)
                .build()


        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(downloadId, notification)
    }

    private fun getIntentToPhoto(file: File): PendingIntent {
        val intent = Intent()
                .apply {
                    action = Intent.ACTION_VIEW
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                    val photoUri = FileProvider.getUriForFile(
                            this@PhotoDownloadJobIntentService,
                            getString(R.string.file_provider_authorities),
                            file
                    )
                    setDataAndType(photoUri, "image/*")
                }

        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
    }

    private fun retrievePhoto(intent: Intent): Photo {
        return intent.getParcelableExtra(EXTRA_PHOTO)
                ?: throw IllegalArgumentException("EXTRA_PHOTO wasnt supplied!")
    }
}