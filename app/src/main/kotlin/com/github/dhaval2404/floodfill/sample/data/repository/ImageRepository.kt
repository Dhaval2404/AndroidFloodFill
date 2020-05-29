package com.github.dhaval2404.floodfill.sample.data.repository

import android.content.Context
import android.graphics.Color
import com.github.dhaval2404.floodfill.sample.data.dao.ImageDAO
import com.github.dhaval2404.floodfill.sample.data.model.Album
import com.github.dhaval2404.floodfill.sample.data.model.AlbumImages
import com.github.dhaval2404.floodfill.sample.data.model.AppColor
import com.github.dhaval2404.floodfill.sample.util.AssetUtil
import com.google.gson.Gson
import org.json.JSONObject
import java.io.IOException
import java.util.Collections
import java.util.Date

/**
 * Image Repository
 *
 * @author Dhaval Patel
 * @version 1.0
 * @since 23 May 2020
 */
interface ImageRepository {

    fun getColors(): List<AppColor>

    fun getAlbums(): List<Album>

}

class ImageRepositoryImpl(
    private val context: Context,
    private val imageDAO: ImageDAO
) : ImageRepository {

    override fun getColors(): List<AppColor> {
        // Read File
        val colorJson = AssetUtil.readFile(context, "material-colors.json")

        // Parse JSON
        val colorMain = JSONObject(colorJson)

        // Prepare result list
        val colors = ArrayList<AppColor>()

        for (colorName in colorMain.keys()) {
            val jsonObject = colorMain.getJSONObject(colorName)

            var palette = ""
            val shades = ArrayList<Int>()

            for (colorCode in jsonObject.keys()) {
                val colorHex = jsonObject.getString(colorCode)

                if (colorCode == "400") {
                    palette = colorHex
                }

                // Ignore color shade start with A
                if (colorCode.contains("a")) continue

                shades.add(Color.parseColor(colorHex))
            }

            colors.add(AppColor(palette, shades))
        }
        return colors
    }

    override fun getAlbums(): List<Album> {
        val albums = readAlbumImages(context)

        albums.forEach { album ->
            album.images.forEach { image ->
                image.albumId = album.id
                image.timestamp = Date()
            }
        }

        if (imageDAO.getCount() <= 0) {
            val imageList = albums.flatMap { it.images }
            imageDAO.insert(imageList)
        }

        return albums
    }

    /*
     * List Asset files
     */
    private fun readAlbumImages(context: Context): List<Album> {
        try {
            val json = AssetUtil.readFile(context, "albums_images.json")
            return Gson().fromJson(json, AlbumImages::class.java).albums
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return Collections.emptyList()
    }
}