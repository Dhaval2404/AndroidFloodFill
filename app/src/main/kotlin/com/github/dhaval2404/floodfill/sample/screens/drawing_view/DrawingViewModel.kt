package com.github.dhaval2404.floodfill.sample.screens.drawing_view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import com.github.dhaval2404.floodfill.sample.data.dao.ImageDAO
import com.github.dhaval2404.floodfill.sample.data.entity.Image
import com.github.dhaval2404.floodfill.sample.data.repository.AppPrefRepository
import com.github.dhaval2404.floodfill.sample.data.repository.ImageRepository
import com.github.dhaval2404.floodfill.sample.screens.color_palette.ColorPaletteAdapter
import com.github.dhaval2404.floodfill.sample.screens.color_palette.ColorShadeAdapter
import com.github.dhaval2404.floodfill.sample.util.BitmapUtil
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.io.File
import java.util.Date

/**
 * @author Dhaval Patel
 * @version 1.0
 * @since 12 Dec 2019
 */
class DrawingViewModel : ViewModel(), KoinComponent {

    private val mImageDAO: ImageDAO by inject()
    private val mImageRepository: ImageRepository by inject()
    private val mAppPrefRepository: AppPrefRepository by inject()

    private val mColorPaletteAdapter: ColorPaletteAdapter by lazy {
        ColorPaletteAdapter()
    }
    private val mColorShadeAdapter: ColorShadeAdapter by lazy {
        ColorShadeAdapter()
    }

    init {
        val colors = mImageRepository.getColors()
        mColorPaletteAdapter.refresh(mImageRepository.getColors())
        val paletteColor = mAppPrefRepository.getRecentPaletteColor() ?: colors.first().palette

        mColorPaletteAdapter.setDefaultPalette(paletteColor)

        val recentColor = getRecentColor()
        if (recentColor != 0) {
            mColorShadeAdapter.setDefaultShade(recentColor)
        } else {
            val shades = colors.first().shades
            mColorShadeAdapter.setDefaultShade(shades[shades.size / 2])
        }
    }

    fun getColorPaletteAdapter() = mColorPaletteAdapter

    fun getColorShadeAdapter() = mColorShadeAdapter

    fun exportDrawing(activity: Activity, bitmap: Bitmap) {
        val file = BitmapUtil.saveBitmap(bitmap)
        file?.let {
            showImage(activity, it)
        }
    }

    fun saveDrawing(context: Context, image: Image?, bitmap: Bitmap) {
        if (image != null) {
            val file = File(context.filesDir, "drawing")
            val path = BitmapUtil.saveBitmap(bitmap, file.absolutePath)

            image.localPath?.let {
                File(it).delete()
            }

            image.timestamp = Date()
            image.localPath = path!!.absolutePath

            mImageDAO.update(image)
        }
    }

    fun getRecentColor() = mAppPrefRepository.getRecentColor()

    fun setRecentColor(color: Int) {
        mAppPrefRepository.setRecentColor(color)
    }

    fun setRecentColorPalette(palette: String) {
        mAppPrefRepository.setRecentPaletteColor(palette)
    }

    private fun showImage(activity: Activity, file: File) {
        val intent = Intent(Intent.ACTION_VIEW)
        val uri = FileProvider.getUriForFile(
            activity, "${activity.packageName}.provider", file
        )
        intent.setDataAndType(uri, "image/*")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        activity.startActivity(intent)
    }

    fun reset(image: Image) {
        image.localPath?.let {
            File(it).delete()
        }
        image.timestamp = Date()
        image.localPath = null
        mImageDAO.update(image)
    }
}
