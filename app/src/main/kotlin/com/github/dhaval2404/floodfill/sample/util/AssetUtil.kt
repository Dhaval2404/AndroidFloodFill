package com.github.dhaval2404.floodfill.sample.util

import android.content.Context

/**
 * @author Dhaval Patel
 * @version 1.0
 * @since 12 Dec 2019
 */
object AssetUtil {

    /**
     * Read Text from Asset Files
     *
     * @param context Application Context
     * @param fileName Asset File Name
     * @return String of file content
     */
    fun readFile(context: Context, fileName: String): String {
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }

}
