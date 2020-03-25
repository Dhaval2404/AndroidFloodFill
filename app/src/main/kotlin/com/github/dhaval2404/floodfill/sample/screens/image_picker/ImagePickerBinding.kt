package com.github.dhaval2404.floodfill.sample.screens.image_picker

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object ImagePickerBinding {

    @JvmStatic
    @BindingAdapter("app:loadImage")
    fun loadImage(imageView: ImageView, path: String) {
        Glide.with(imageView.context)
            .load(path)
            .centerInside()
            .into(imageView)
    }

}