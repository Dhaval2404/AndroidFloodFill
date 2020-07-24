package com.github.dhaval2404.floodfill.sample.screens.image_picker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.github.dhaval2404.floodfill.sample.R
import com.github.dhaval2404.floodfill.sample.data.model.Album
import com.github.dhaval2404.floodfill.sample.screens.base.BaseActivity
import kotlinx.android.synthetic.main.activity_image_picker.*
import org.koin.android.ext.android.get

class ImagePickerActivity : BaseActivity<ImagePickerViewModel>(R.layout.activity_image_picker), ImagePickerNavigator {

    override fun getViewModel(): ImagePickerViewModel = get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel.setNavigator(this)
        mViewModel.setBundle(intent.extras)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.title = mViewModel.getTitle()

        imageRV.adapter = mViewModel.getAdapter()

        imageRV.post {
            // Show RecyclerView Item Added Animation
            imageRV.adapter?.notifyDataSetChanged()
            imageRV.scheduleLayoutAnimation()
        }

    }

    companion object {
        const val EXTRA_ALBUM = "extra.album"

        fun getIntent(context: Context, album: Album): Intent {
            return Intent(context, ImagePickerActivity::class.java).apply {
                putExtra(EXTRA_ALBUM, album)
            }
        }
    }

}
