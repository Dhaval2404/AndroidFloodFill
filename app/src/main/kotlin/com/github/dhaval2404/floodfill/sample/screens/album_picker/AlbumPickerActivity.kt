package com.github.dhaval2404.floodfill.sample.screens.album_picker

import android.os.Bundle
import com.github.dhaval2404.floodfill.sample.R
import com.github.dhaval2404.floodfill.sample.screens.base.BaseActivity
import kotlinx.android.synthetic.main.activity_album_picker.*
import org.koin.android.ext.android.get

/**
 * Album Picker Activity
 *
 * @author Dhaval Patel
 * @version 1.0
 * @since 27 Dec 2019
 */
class AlbumPickerActivity : BaseActivity<AlbumPickerViewModel>(R.layout.activity_album_picker),
    AlbumPickerNavigator {

    override fun getViewModel(): AlbumPickerViewModel = get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel.setNavigator(this)
        lifecycle.addObserver(mViewModel)

        imageCategoryRV.adapter = mViewModel.getAdapter()
    }

}