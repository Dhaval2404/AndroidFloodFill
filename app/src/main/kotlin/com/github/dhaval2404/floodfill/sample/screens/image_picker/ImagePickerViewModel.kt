package com.github.dhaval2404.floodfill.sample.screens.image_picker

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.github.dhaval2404.floodfill.sample.model.Image
import com.github.dhaval2404.floodfill.sample.screens.drawing.DrawingActivity
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * TODO: Add Class Header
 *
 * @author Dhaval Patel
 * @version 1.0
 * @since 27 Dec 2019
 */
class ImagePickerViewModel() :ViewModel(), KoinComponent{

    private var mNavigator: ImagePickerNavigator? = null
    private val mContext:Context by inject()

    private val mImagePickerAdapter: ImagePickerAdapter by lazy {
        ImagePickerAdapter(this)
    }

    fun setNavigator(navigator: ImagePickerNavigator) {
        this.mNavigator = navigator
    }

    fun getAdapter() = mImagePickerAdapter

    fun onImagePick(image: Image) {
        val intent = Intent(mContext, DrawingActivity::class.java)
        intent.putExtra("path", image.url)
        mNavigator?.startActivity(intent)
    }

}
