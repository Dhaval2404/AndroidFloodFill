package com.github.dhaval2404.floodfill.sample.screens.image_picker

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.github.dhaval2404.floodfill.sample.data.dao.ImageDAO
import com.github.dhaval2404.floodfill.sample.data.entity.Image
import com.github.dhaval2404.floodfill.sample.data.model.Album
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.io.File

object ImagePickerBinding : KoinComponent {

    private val mImageDAO: ImageDAO by inject()

    @JvmStatic
    @BindingAdapter("app:loadImage")
    fun loadImage(imageView: ImageView, path: String) {
        Glide.with(imageView.context)
            .load(path)
            .centerInside()
            .into(imageView)
    }

    @JvmStatic
    @BindingAdapter("app:loadImage")
    fun loadImage(imageView: ImageView, album: Album) {
        val image = mImageDAO.getLastModifiedImage(album.id)
        loadImage(imageView, image)
    }

    @JvmStatic
    @BindingAdapter("app:loadImage")
    fun loadImage(imageView: ImageView, image: Image) {
        val builder = Glide.with(imageView.context)
        val localPath = image.localPath
        val loader = if (localPath != null) {
            builder.load(File(localPath))
        } else {
            builder.load(image.url)
        }
        loader.centerInside()
            .into(imageView)
    }

}