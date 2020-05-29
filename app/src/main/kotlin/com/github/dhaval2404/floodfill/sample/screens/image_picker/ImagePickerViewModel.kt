package com.github.dhaval2404.floodfill.sample.screens.image_picker

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.github.dhaval2404.floodfill.sample.data.dao.ImageDAO
import com.github.dhaval2404.floodfill.sample.data.entity.Image
import com.github.dhaval2404.floodfill.sample.data.model.Album
import com.github.dhaval2404.floodfill.sample.screens.base.BaseViewModel
import com.github.dhaval2404.floodfill.sample.screens.drawing_view.DrawingActivity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.launch
import org.koin.core.inject
import java.util.stream.Collectors

/**
 * TODO: Add Class Header
 *
 * @author Dhaval Patel
 * @version 1.0
 * @since 27 Dec 2019
 */
class ImagePickerViewModel() : BaseViewModel<ImagePickerNavigator>() {

    private val mContext: Context by inject()
    private val mImageDAO: ImageDAO by inject()

    private val mImagePickerAdapter = ImagePickerAdapter()
    private var mAlbum: Album? = null

    init {
        mImagePickerAdapter.setImageClickListener(::onImagePick)
    }

    fun setBundle(bundle: Bundle?) {
        mAlbum = bundle?.getParcelable(ImagePickerActivity.EXTRA_ALBUM)
        setImages()
    }

    fun getAdapter() = mImagePickerAdapter

    fun getTitle() = mAlbum?.title

    fun setImages() {
        viewModelScope.launch {
            val imageList = mImageDAO.getByAlbumId(mAlbum?.id ?: 0)
            imageList.collect {
                mImagePickerAdapter.refresh(it)
            }
        }
    }

    private fun onImagePick(image: Image) {
        val intent = DrawingActivity.getIntent(mContext, image)
        mNavigator?.startActivity(intent)
    }

}
