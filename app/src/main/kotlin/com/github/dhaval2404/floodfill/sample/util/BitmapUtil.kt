package com.github.dhaval2404.floodfill.sample.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
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

    fun saveBitmap(finalBitmap: Bitmap): File? {
        val root: String = Environment.getExternalStorageDirectory().absolutePath
        val fname = "Image_" + System.currentTimeMillis() + ".png"
        val file = File(root, fname)
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.flush()
            out.close()

            return file
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun isEqualColor(color1: Int, color2: Int, tolerance: Int = 50): Boolean {
        val alpha1 = Color.alpha(color1)
        val red1 = Color.red(color1)
        val green1 = Color.green(color1)
        val blue1 = Color.blue(color1)

        val alpha2 = Color.alpha(color2)
        val red2 = Color.red(color2)
        val green2 = Color.green(color2)
        val blue2 = Color.blue(color2)

        return (alpha1 >= (alpha2 - tolerance) &&
                alpha1 <= (alpha2 + tolerance) &&
                red1 >= (red2 - tolerance) &&
                red1 <= (red2 + tolerance) &&
                green1 >= (green2 - tolerance) &&
                green1 <= (green2 + tolerance) &&
                blue1 >= (blue2 - tolerance) &&
                blue1 <= (blue2 + tolerance))
    }

}
