package com.github.dhaval2404.floodfill.sample.screens.drawing

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import com.github.dhaval2404.floodfill.sample.model.AppColor
import com.github.dhaval2404.floodfill.sample.screens.color_palette.ColorPaletteAdapter
import com.github.dhaval2404.floodfill.sample.screens.color_palette.ColorShadeAdapter
import com.github.dhaval2404.floodfill.sample.util.AssetUtil
import com.github.dhaval2404.floodfill.sample.util.BitmapUtil
import org.json.JSONObject
import java.io.File

/**
 * @author Dhaval Patel
 * @version 1.0
 * @since 12 Dec 2019
 */
class DrawingViewModel : ViewModel() {

    private val mColorPaletteAdapter: ColorPaletteAdapter by lazy {
        ColorPaletteAdapter()
    }
    private val mColorShadeAdapter: ColorShadeAdapter by lazy {
        ColorShadeAdapter()
    }

    init {
        mColorPaletteAdapter.setColorPaletteListener { color ->
            mColorShadeAdapter.refresh(color.shades)
        }
    }

    fun getColorPaletteAdapter() = mColorPaletteAdapter

    fun getColorShadeAdapter() = mColorShadeAdapter

    fun setColorChangeListener(listener: (Int) -> Unit) {
        mColorShadeAdapter.setColorShadeListener(listener)
    }

    fun loadColors(context: Context) {
        mColorPaletteAdapter.refresh(getColors(context))
    }

    private fun getColors(context: Context): List<AppColor> {
        val colorJson = AssetUtil.readFile(context, "material-colors.json")
        val colorMain = JSONObject(colorJson)
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

                // if (colorCode.contains("a") || colorCode == "50") continue
                if (colorCode.contains("a")) continue
                shades.add(Color.parseColor(colorHex))
            }

            colors.add(AppColor(palette, shades))
        }
        return colors
    }

    fun saveDrawing(activity: Activity, bitmap: Bitmap) {
        val file = BitmapUtil.saveBitmap(bitmap)
        file?.let {
            showImage(activity, it)
        }
    }

    private fun showImage(activity: Activity, file: File) {
        val intent = Intent(Intent.ACTION_VIEW)
        val uri = FileProvider.getUriForFile(
            activity,
            "${activity.packageName}.provider", file
        )
        intent.setDataAndType(uri, "image/*")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        activity.startActivity(intent)
    }
}
