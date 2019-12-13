package com.github.dhaval2404.floodfill

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Point
import android.os.Handler

/**
 * @author Dhaval Patel
 * @version 1.0
 * @since 11 Dec 2019
 */
class FloodFill private constructor(
    val canvas: Bitmap,
    val point: Point,
    val targetColor: Int,
    val tolerance: Int,
    val newColor: Int
) {

    data class Builder(
        /**
         * Image Bitmap on which flood-fill algorithms will be applied
         **/
        var canvas: Bitmap,
        /**
         * Touch point on bitmap image. Point must be inside image size
         */
        var point: Point = Point(0, 0),
        /**
         * Color to fill in close regions
         */
        var newColor: Int = Color.BLUE, // Default Blue
        /**
         * Color on which the newColor will be applied
         */
        var targetColor: Int = 0, // Optional
        /**
         * Tolerance for the flood-fill algorithms
         */
        var tolerance: Int = 150 // Optional
    ) {

        fun point(x: Float, y: Float) = apply { this.point = Point(x.toInt(), y.toInt()) }

        fun point(x: Int, y: Int) = apply { this.point = Point(x, y) }

        fun newColor(newColor: Int) = apply { this.newColor = newColor }

        fun targetColor(targetColor: Int) = apply { this.targetColor = targetColor }

        fun tolerance(tolerance: Int) = apply { this.tolerance = tolerance }

        fun build() = FloodFill(canvas, point, targetColor, tolerance, newColor)
    }

    companion object {
        init {
            System.loadLibrary("floodfill")
        }
    }

    private external fun floodFill(
        bitmap: Bitmap,
        x: Int,
        y: Int,
        fillColor: Int,
        targetColor: Int,
        tolerance: Int
    ): FloatArray

    fun getPixels(): FloatArray {
        return floodFill(
            canvas,
            point.x,
            point.y,
            newColor,
            targetColor,
            tolerance
        )
    }

    fun getPixels(handler: Handler) {
        Thread(Runnable {
            val pixels = getPixels()
            publishResult(handler, pixels)
        }).start()
    }

    private fun publishResult(handler: Handler, result: Any) {
        handler.obtainMessage().apply {
            what = 0
            arg1 = 1
            obj = result
        }.also {
            handler.sendMessage(it)
        }
    }
}
