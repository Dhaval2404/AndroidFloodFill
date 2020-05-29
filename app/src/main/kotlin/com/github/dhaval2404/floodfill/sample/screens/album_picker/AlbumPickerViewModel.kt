package com.github.dhaval2404.floodfill.sample.screens.album_picker

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.github.dhaval2404.floodfill.sample.data.model.Album
import com.github.dhaval2404.floodfill.sample.data.repository.ImageRepository
import com.github.dhaval2404.floodfill.sample.screens.base.BaseViewModel
import com.github.dhaval2404.floodfill.sample.screens.image_picker.ImagePickerActivity
import org.koin.core.inject

/**
 * Album Picker ViewModel
 *
 * @author Dhaval Patel
 * @version 1.0
 * @since 27 Dec 2019
 */
class AlbumPickerViewModel : BaseViewModel<AlbumPickerNavigator>(), LifecycleObserver {

    private val mContext: Context by inject()
    private val mImageRepository: ImageRepository by inject()

    // Album Adapter
    private val mAlbumAdapter = AlbumPickerAdapter()

    init {
        mAlbumAdapter.setAlbumClickListener(::onAlbumClick)
        mAlbumAdapter.refresh(mImageRepository.getAlbums())
    }

    fun getAdapter() = mAlbumAdapter

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun refresh() {
        mAlbumAdapter.notifyDataSetChanged()
    }

    private fun onAlbumClick(album: Album) {
        val intent = ImagePickerActivity.getIntent(mContext, album)
        this.mNavigator?.startActivity(intent)
    }

}
