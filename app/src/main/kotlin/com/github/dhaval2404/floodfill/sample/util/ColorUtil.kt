package com.github.dhaval2404.floodfill.sample.util

import android.graphics.Color

/**
 * Color Utility
 *
 * @author Dhaval Patel
 * @version 1.0
 * @since 23 May 2020
 */
object ColorUtil {

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