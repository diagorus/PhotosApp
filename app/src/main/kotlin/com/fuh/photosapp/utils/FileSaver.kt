package com.fuh.photosapp.utils

import android.content.Context
import android.os.Environment
import com.fuh.photosapp.R
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream

/**
 * Created by Nikita on 21.04.18.
 */
class FileSaver(private val ctx: Context) {

    fun saveFileToPublic(fileName: String, fileBytes: ByteArray): File {
        val rootDir = File(Environment.getExternalStorageDirectory(), ctx.getString(R.string.app_name))
        if (!rootDir.mkdirs()) {
            Timber.e("App directory wasnt created!")
        }

        val outputFile = File(rootDir, fileName)
        FileOutputStream(outputFile).use {
            it.write(fileBytes)
        }

        return outputFile
    }
}