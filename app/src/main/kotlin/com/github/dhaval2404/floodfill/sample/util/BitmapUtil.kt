package com.github.dhaval2404.floodfill.sample.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.RectF
import android.os.Environment
import android.view.View
import java.io.File
import java.io.FileOutputStream

/**
 * @author Dhaval Patel
 * @version 1.0
 * @since 22-11-2019
 */
object BitmapUtil {

    fun saveBitmap(bitmap: Bitmap): File? {
        val root: String = Environment.getExternalStorageDirectory().absolutePath
        return saveBitmap(bitmap, root)
    }

    fun saveBitmap(bitmap: Bitmap, root: String): File? {
        val rootFile = File(root)
        if (!rootFile.exists()) rootFile.mkdirs()

        val fname = "Image_" + System.currentTimeMillis() + ".png"
        val file = File(root, fname)
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.flush()
            out.close()

            return file
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getBitmapMatrix(bitmap: Bitmap, width: Float, height: Float): Matrix {
        val src = RectF(
            0f, 0f,
            bitmap.width.toFloat(),
            bitmap.height.toFloat()
        )

        val dst = RectF(0f, 0f, width, height)

        return Matrix().apply {
            setRectToRect(src, dst, Matrix.ScaleToFit.CENTER)
        }
    }

    fun getBitmap(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.layout(view.left, view.top, view.right, view.bottom)
        view.draw(canvas)
        return bitmap
    }

}
