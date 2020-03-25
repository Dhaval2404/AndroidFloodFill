package com.github.dhaval2404.floodfill.sample.model

import android.graphics.Color

/**
 * @author Dhaval Patel
 * @version 1.0
 * @since 12 Dec 2019
 */
data class AppColor(
    val palette: String,
    val shades: List<Int>
) {

    val paletteColor
        get() = Color.parseColor(palette)
}
