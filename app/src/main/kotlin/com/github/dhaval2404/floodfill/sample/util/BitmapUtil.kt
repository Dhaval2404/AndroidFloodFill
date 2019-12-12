package com.github.dhaval2404.floodfill.sample.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.IOException
import java.io.InputStream

/**
 * @author Dhaval Patel
 * @version 1.0
 * @since 22-11-2019
 */
object BitmapUtil {

    fun getBitmapFromAsset(context: Context, filePath: String): Bitmap? {
        val assetManager = context.assets

        val ip: InputStream
        var bitmap: Bitmap? = null
        try {
            ip = assetManager.open(filePath)
            bitmap = BitmapFactory.decodeStream(ip)
        } catch (e: IOException) {
            // handle exception
            e.printStackTrace()
        }

        return bitmap
    }

    fun resize(bitmap: Bitmap, dstWidth: Int, dstHeight: Int): Bitmap {
        return Bitmap.createScaledBitmap(bitmap, dstWidth, dstHeight, false)
    }

}
